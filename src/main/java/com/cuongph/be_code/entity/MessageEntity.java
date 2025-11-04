package com.cuongph.be_code.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "messages")
public class MessageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomEntity chatRoomEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    private boolean read = false;

    public MessageEntity() {
        this.sentAt = LocalDateTime.now();
    }
}
