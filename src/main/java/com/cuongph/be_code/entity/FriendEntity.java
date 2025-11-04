package com.cuongph.be_code.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "friends")
public class FriendEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người gửi lời mời
    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    // Người nhận lời mời
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    // Trạng thái: PENDING, ACCEPTED, REJECTED, BLOCKED
    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;
}
