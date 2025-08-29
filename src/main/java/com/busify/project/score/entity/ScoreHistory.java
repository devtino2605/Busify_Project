package com.busify.project.score.entity;

import com.busify.project.booking.entity.Bookings;
import com.busify.project.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "score_history", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"booking_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Bookings booking;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "points_added", nullable = false)
    private Integer pointsAdded;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();
}
