package com.busify.project.complaint.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.busify.project.user.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.busify.project.complaint.entity.Complaint;
import com.busify.project.complaint.enums.ComplaintStatus;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findAllByStatus(ComplaintStatus status);

    List<Complaint> findAllByCustomerId(Long customerId);

    List<Complaint> findAllByAssignedAgent_Id(Long assignedAgentId);

    // find by assigned agent email
    List<Complaint> findAllByAssignedAgent_Email(String email);

    List<Complaint> findAllByBookingId(Long bookingId);

    List<Complaint> findAllByTitleContainingIgnoreCase(String title);

    List<Complaint> findAllByDescriptionContainingIgnoreCase(String description);

    @Query("SELECT c FROM Complaint c WHERE c.booking.trip.bus.operator.id = :busOperatorId")
    List<Complaint> findByBusOperatorComplaint(@Param("busOperatorId") Long busOperatorId);

    @Query("SELECT c FROM Complaint c WHERE c.booking.trip.id = :tripId")
    List<Complaint> findByTripId(@Param("tripId") Long tripId);

    // Đếm số khiếu nại đang xử lý của một nhân viên
    long countByAssignedAgent_IdAndStatus(Long agentId, ComplaintStatus status);

    // Đếm số complaint theo agent, trạng thái, và khoảng thời gian
    long countByAssignedAgent_IdAndStatusAndCreatedAtBetween(Long agentId, ComplaintStatus status, LocalDateTime start,
            LocalDateTime end);

    /**
     * Tìm một khiếu nại có status = 'New' và chưa được gán.
     * Sắp xếp theo created_at để ưu tiên xử lý khiếu nại cũ trước.
     * 
     * @Lock(LockModeType.PESSIMISTIC_WRITE) sẽ tạo ra câu lệnh "SELECT ... FOR
     *                                       UPDATE"
     *                                       trong SQL. Khi một transaction đọc dòng
     *                                       này, nó sẽ bị khóa lại, các
     *                                       transaction khác phải chờ hoặc sẽ bị bỏ
     *                                       qua (tùy cấu hình).
     *
     *                                       Ghi chú: Để có hiệu năng tốt nhất với
     *                                       nhiều nhân viên cùng lấy việc,
     *                                       bạn có thể cần hint "SKIP LOCKED" của
     *                                       DB.
     *                                       Ví dụ với native query cho PostgreSQL:
     * @Query(value = "SELECT * FROM complaints WHERE status = 'New' AND
     *              assigned_agent_id IS NULL ORDER BY created_at ASC LIMIT 1 FOR
     *              UPDATE SKIP LOCKED", nativeQuery = true)
     */
    @Query(value = "SELECT * FROM complaints c WHERE c.status = 'NEW' AND c.assigned_agent_id IS NULL ORDER BY c.created_at ASC LIMIT 1 FOR UPDATE", nativeQuery = true)
    Optional<Complaint> findNextUnassignedComplaint();

    List<Complaint> findAllByAssignedAgent(User user);

    // Lấy danh sách complaints theo AssignedAgent và trạng thái in_progress
    List<Complaint> findAllByAssignedAgentAndStatus(User assignedAgent, ComplaintStatus status);

    // @Lock(LockModeType.PESSIMISTIC_WRITE)
    // @Query(value = "SELECT * FROM complaints c WHERE c.status = 'NEW' AND
    // c.assigned_agent_id IS NULL ORDER BY c.created_at ASC LIMIT 1", nativeQuery =
    // true)
    // Optional<Complaint> findNextUnassignedComplaint();
}
