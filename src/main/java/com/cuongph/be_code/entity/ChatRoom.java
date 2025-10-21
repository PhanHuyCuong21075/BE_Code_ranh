package com.cuongph.be_code.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tên phòng (dùng cho nhóm chat)
    private String name;

    // Thời gian tạo phòng chat
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // User tham gia phòng chat (many-to-many)
    @ManyToMany
    @JoinTable(
            name = "chat_room_users",
            joinColumns = @JoinColumn(name = "chat_room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants;
}
