package com.busify.project.chat.service;

import com.busify.project.chat.dto.ChatMessageDTO;
import com.busify.project.chat.model.ChatMessage;
import com.busify.project.chat.repository.ChatMessageRepository;
import com.busify.project.user.entity.Profile;
import com.busify.project.user.entity.User;
import com.busify.project.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatAssignmentService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final Integer CUSTOMER_SERVICE_ROLE_ID = 11;
    private static final int MAX_CHATS_PER_AGENT = 10; // Tăng giới hạn từ 5 lên 10
    private final AtomicInteger roundRobinCounter = new AtomicInteger(0);

    /**
     * Gán một phòng chat chưa được phân công cho một nhân viên phù hợp.
     * Việc "gán" được thực hiện bằng cách gửi một tin nhắn tự động từ nhân viên đó.
     *
     * @param roomId    ID của phòng chat cần gán.
     * @param userEmail Email của khách hàng đã bắt đầu cuộc trò chuyện.
     * @return Tin nhắn đã được lưu, đại diện cho việc gán.
     */
    @Transactional
    public Optional<ChatMessage> assignChatToAvailableAgent(String roomId, String userEmail) {
        // 1. Tìm nhân viên phù hợp
        Optional<User> selectedAgentOptional = findBestAvailableAgent();

        if (selectedAgentOptional.isEmpty()) {
            System.out.println("Không có nhân viên nào sẵn sàng nhận thêm cuộc trò chuyện.");
            // Có thể thêm logic thông báo cho khách hàng chờ đợi ở đây
            return Optional.empty();
        }

        User agent = selectedAgentOptional.get();

        // 2. Tạo tin nhắn "gán" từ nhân viên được chọn
        // Cast agent to Profile to access fullName (assuming agents are Profile
        // instances)
        String fullName = (agent instanceof Profile) ? ((Profile) agent).getFullName() : "Agent";
        ChatMessageDTO assignmentMessageDTO = ChatMessageDTO.builder()
                .content("Xin chào, tôi là " + fullName + ". Tôi sẽ hỗ trợ bạn ngay.")
                .sender(agent.getEmail()) // Người gửi là nhân viên được gán
                .recipient(userEmail) // Người nhận là khách hàng
                .type(ChatMessageDTO.MessageType.SYSTEM_ASSIGN) // Kiểu tin nhắn mới để phân biệt
                .build();

        ChatMessage assignmentMessage = ChatMessage.builder()
                .content(assignmentMessageDTO.getContent())
                .sender(assignmentMessageDTO.getSender())
                .recipient(assignmentMessageDTO.getRecipient())
                .type(assignmentMessageDTO.getType())
                .roomId(roomId)
                .timestamp(LocalDateTime.now())
                .build();

        // 3. Lưu và gửi tin nhắn
        ChatMessage savedMessage = chatMessageRepository.save(assignmentMessage);
        messagingTemplate.convertAndSend("/topic/public/" + roomId, savedMessage);

        long currentWorkload = chatMessageRepository.countDistinctRoomIdBySenderAndType(agent.getEmail(),
                ChatMessageDTO.MessageType.SYSTEM_ASSIGN);
        System.out.println("Đã gán phòng chat ID: " + roomId +
                " cho nhân viên: " + agent.getEmail() +
                " (Workload hiện tại: " + currentWorkload + " chats)");

        return Optional.of(savedMessage);
    }

    /**
     * Tìm customer_service đã được assign cho room này
     */
    public Optional<String> getAssignedAgentForRoom(String roomId) {
        System.out.println("🔍 Tìm agent đã assign cho room: " + roomId);
        
        // Tìm tin nhắn SYSTEM_ASSIGN đầu tiên trong room để xác định agent đã được gán
        List<ChatMessage> assignmentMessages = chatMessageRepository.findByRoomIdAndTypeOrderByTimestampAsc(
                roomId, ChatMessageDTO.MessageType.SYSTEM_ASSIGN);
        
        if (!assignmentMessages.isEmpty()) {
            String agentEmail = assignmentMessages.get(0).getSender();
            System.out.println("✅ Tìm thấy agent đã assign: " + agentEmail);
            return Optional.of(agentEmail);
        }
        
        System.out.println("❌ Không tìm thấy agent nào đã assign cho room");
        return Optional.empty();
    }

    /**
     * Tìm nhân viên tốt nhất để gán cuộc trò chuyện.
     */
    private Optional<User> findBestAvailableAgent() {
        System.out.println("🔍 Bắt đầu tìm customer_service available...");
        
        List<User> agents = userRepository.findUsersByRoleId(CUSTOMER_SERVICE_ROLE_ID);
        System.out.println("📊 Tìm thấy " + agents.size() + " customer_service trong database");
        
        if (agents.isEmpty()) {
            System.out.println("❌ Không có customer_service nào trong database");
            return Optional.empty();
        }

        // Log thông tin từng agent
        for (User agent : agents) {
            if (agent instanceof Profile) {
                Profile profile = (Profile) agent;
                System.out.println("👤 Agent: " + agent.getEmail() + 
                                 " - Name: " + profile.getFullName() + 
                                 " - Status: " + profile.getStatus());
            } else {
                System.out.println("👤 Agent: " + agent.getEmail() + " (không phải Profile)");
            }
        }

        // Lọc các nhân viên có trạng thái active
        List<User> activeAgents = agents.stream()
                .filter(agent -> {
                    if (agent instanceof Profile) {
                        Profile profile = (Profile) agent;
                        boolean isActive = profile.getStatus() != null && 
                                         profile.getStatus().name().equalsIgnoreCase("active");
                        System.out.println("✅ Agent " + agent.getEmail() + " active check: " + isActive);
                        return isActive;
                    }
                    System.out.println("⚠️ Agent " + agent.getEmail() + " không phải Profile, coi như active");
                    return true; // Nếu không phải Profile thì coi như active
                })
                .collect(Collectors.toList());

        System.out.println("📈 Có " + activeAgents.size() + " customer_service active");

        if (activeAgents.isEmpty()) {
            System.out.println("❌ Không có customer_service nào active");
            return Optional.empty();
        }

        // Lọc các nhân viên chưa đạt giới hạn tối đa
        List<User> availableAgents = activeAgents.stream()
                .filter(agent -> {
                    // Workload được tính bằng số phòng chat mà agent đã được "gán"
                    long workload = chatMessageRepository.countDistinctRoomIdBySenderAndType(
                            agent.getEmail(), ChatMessageDTO.MessageType.SYSTEM_ASSIGN);
                    boolean available = workload < MAX_CHATS_PER_AGENT;
                    System.out.println("📊 Agent " + agent.getEmail() + 
                                     " - Workload: " + workload + "/" + MAX_CHATS_PER_AGENT + 
                                     " - Available: " + available);
                    return available;
                })
                .collect(Collectors.toList());

        System.out.println("🎯 Có " + availableAgents.size() + " customer_service available");

        if (availableAgents.isEmpty()) {
            System.out.println("❌ Tất cả customer_service đều đã đạt giới hạn workload");
            return Optional.empty();
        }

        // Sử dụng chiến lược lai: Round-robin và cân bằng tải
        return selectAgentWithHybridStrategy(availableAgents);
    }

    /**
     * Chiến lược lai: Round-robin với cân bằng tải.
     */
    private Optional<User> selectAgentWithHybridStrategy(List<User> availableAgents) {
        if (availableAgents.size() == 1) {
            return Optional.of(availableAgents.get(0));
        }

        // Tìm workload thấp nhất
        long minWorkload = availableAgents.stream()
                .mapToLong(agent -> chatMessageRepository.countDistinctRoomIdBySenderAndType(
                        agent.getEmail(), ChatMessageDTO.MessageType.SYSTEM_ASSIGN))
                .min()
                .orElse(0);

        // Lấy danh sách các nhân viên có workload thấp nhất
        List<User> leastBusyAgents = availableAgents.stream()
                .filter(agent -> {
                    long workload = chatMessageRepository.countDistinctRoomIdBySenderAndType(
                            agent.getEmail(), ChatMessageDTO.MessageType.SYSTEM_ASSIGN);
                    return workload == minWorkload;
                })
                .collect(Collectors.toList());

        if (leastBusyAgents.size() == 1) {
            return Optional.of(leastBusyAgents.get(0));
        }

        // Nếu có nhiều nhân viên cùng workload thấp nhất, dùng round-robin
        int index = roundRobinCounter.getAndIncrement() % leastBusyAgents.size();
        return Optional.of(leastBusyAgents.get(index));
    }
}