package com.busify.project.complaint.service;

import com.busify.project.complaint.entity.Complaint;
import com.busify.project.complaint.enums.ComplaintStatus;
import com.busify.project.complaint.repository.ComplaintRepository;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ComplaintAssignmentService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    private static final Long CUSTOMER_SERVICE_ROLE_ID = 2L;
    private static final int MAX_COMPLAINTS_PER_AGENT = 10; // Giới hạn số complaint mỗi agent
    private final AtomicInteger roundRobinCounter = new AtomicInteger(0);
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_NEW = "New";

    /**
     * Phương thức chính để gán một khiếu nại cho nhân viên phù hợp.
     * 
     * @Transactional là BẮT BUỘC. Nó đảm bảo rằng toàn bộ quá trình
     *                (tìm, khóa, cập nhật) diễn ra trong một giao dịch duy nhất.
     *                Nếu có lỗi, mọi thứ sẽ được rollback.
     */
    @Transactional
    public Optional<Complaint> assignComplaintToAvailableAgent() {
        // 1. Tìm một khiếu nại mới nhất để gán (đã được khóa bởi repository)
        Optional<Complaint> complaintOptional = complaintRepository.findNextUnassignedComplaint();

        if (complaintOptional.isEmpty()) {
            System.out.println("Không có khiếu nại nào để gán");
            return Optional.empty();
        }

        // 2. Tìm nhân viên phù hợp với cơ chế phân phối công bằng
        Optional<User> selectedAgentOptional = findBestAvailableAgent();

        if (selectedAgentOptional.isEmpty()) {
            System.out.println("Không có nhân viên nào sẵn sàng nhận thêm khiếu nại");
            return Optional.empty();
        }

        Complaint complaintToAssign = complaintOptional.get();
        User agent = selectedAgentOptional.get();

        // 3. Gán việc
        complaintToAssign.setAssignedAgent(agent);
        complaintToAssign.setStatus(ComplaintStatus.in_progress);
        complaintToAssign.setUpdatedAt(LocalDateTime.now());

        Complaint savedComplaint = complaintRepository.save(complaintToAssign);

        // Log thông tin gán việc
        long currentWorkload = complaintRepository.countByAssignedAgent_IdAndStatus(agent.getId(),
                ComplaintStatus.in_progress);
        System.out.println("Đã gán khiếu nại ID: " + savedComplaint.getComplaintsId() +
                " cho nhân viên: " + agent.getEmail() +
                " (Workload hiện tại: " + currentWorkload + ")");

        return Optional.of(savedComplaint);
    }

    /**
     * Tìm nhân viên tốt nhất để gán khiếu nại với cơ chế phân phối công bằng
     */
    private Optional<User> findBestAvailableAgent() {
        List<User> agents = userRepository.findByRoleId(CUSTOMER_SERVICE_ROLE_ID);

        if (agents.isEmpty()) {
            return Optional.empty();
        }

        // Lọc các nhân viên chưa đạt giới hạn tối đa
        List<User> availableAgents = agents.stream()
                .filter(agent -> {
                    long workload = complaintRepository.countByAssignedAgent_IdAndStatus(
                            agent.getId(), ComplaintStatus.in_progress);
                    return workload < MAX_COMPLAINTS_PER_AGENT;
                })
                .toList();

        if (availableAgents.isEmpty()) {
            System.out.println("Tất cả nhân viên đã đạt giới hạn tối đa khiếu nại");
            return Optional.empty();
        }

        // Sử dụng kết hợp giữa round-robin và workload balancing
        return selectAgentWithHybridStrategy(availableAgents);
    }

    /**
     * Chiến lược lai: Round-robin với cân bằng tải
     */
    private Optional<User> selectAgentWithHybridStrategy(List<User> availableAgents) {
        if (availableAgents.size() == 1) {
            return Optional.of(availableAgents.get(0));
        }

        // Tìm nhân viên có workload thấp nhất
        long minWorkload = availableAgents.stream()
                .mapToLong(agent -> complaintRepository.countByAssignedAgent_IdAndStatus(
                        agent.getId(), ComplaintStatus.in_progress))
                .min()
                .orElse(0);

        // Lấy danh sách các nhân viên có workload thấp nhất
        List<User> leastBusyAgents = availableAgents.stream()
                .filter(agent -> {
                    long workload = complaintRepository.countByAssignedAgent_IdAndStatus(
                            agent.getId(), ComplaintStatus.in_progress);
                    return workload == minWorkload;
                })
                .toList();

        // Nếu chỉ có 1 nhân viên có workload thấp nhất, chọn ngay
        if (leastBusyAgents.size() == 1) {
            return Optional.of(leastBusyAgents.get(0));
        }

        // Nếu có nhiều nhân viên cùng workload thấp nhất, dùng round-robin
        int index = roundRobinCounter.getAndIncrement() % leastBusyAgents.size();
        User selectedAgent = leastBusyAgents.get(index);

        System.out.println("Chọn nhân viên bằng round-robin: " + selectedAgent.getEmail() +
                " (Index: " + index + "/" + leastBusyAgents.size() + ")");

        return Optional.of(selectedAgent);
    }

    /**
     * Method cũ - giữ lại để tham khảo
     */
    private Optional<User> findLeastBusyAgent() {
        List<User> agents = userRepository.findByRoleId(CUSTOMER_SERVICE_ROLE_ID);

        if (agents.isEmpty()) {
            return Optional.empty();
        }

        return agents.stream()
                .min(Comparator.comparingLong(agent -> complaintRepository
                        .countByAssignedAgent_IdAndStatus(agent.getId(), ComplaintStatus.pending)));
    }
}