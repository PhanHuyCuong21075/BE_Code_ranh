package com.cuongph.be_code.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "comments")
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Người bình luận
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Bài viết bình luận
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity postEntity;

    @Column(columnDefinition = "TEXT")
    private String content;

    // Ảnh kèm theo bình luận (có thể null)
    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
