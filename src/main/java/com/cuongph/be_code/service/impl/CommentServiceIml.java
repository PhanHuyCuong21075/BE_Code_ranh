package com.cuongph.be_code.service.impl;

import com.cuongph.be_code.dto.request.CommentCreateRequest;
import com.cuongph.be_code.dto.response.CommentsResponse;
import com.cuongph.be_code.entity.CommentEntity;
import com.cuongph.be_code.entity.PostEntity;
import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.repo.CommentRepository;
import com.cuongph.be_code.repo.UserRepository;
import com.cuongph.be_code.service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.cuongph.be_code.common.auth.AuthService.getCurrentUsername;

@Service
public class CommentServiceIml implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentServiceIml(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CommentEntity createOrUpdateComment(CommentCreateRequest request) {

        String currentUsername = getCurrentUsername();

        UserEntity user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CommentEntity comment;

        // 👉 CREATE
        if (request.getCommentId() == null) {
            comment = new CommentEntity();
            comment.setPostId(request.getPostId());
            comment.setUserId(user.getId());
            comment.setCreatedAt(LocalDateTime.now());
            comment.setCreatedBy(currentUsername);
        }
        // 👉 UPDATE
        else {
            comment = commentRepository.findById(request.getCommentId())
                    .orElseThrow(() -> new RuntimeException("Comment not found"));

            if (!comment.getUserId().equals(user.getId())) {
                throw new RuntimeException("You are not allowed to edit this comment");
            }

            comment.setCreatedAt(LocalDateTime.now());
            comment.setUpdateBy(currentUsername);
        }

        // 👉 Fields dùng chung
        comment.setContent(request.getContent());
        comment.setImageUrl(request.getImageUrl());

        return commentRepository.save(comment);
    }

    @Override
    public List<CommentsResponse> commentsInPost(Long postId) {
        return commentRepository.findCommentsByPostId(postId);
    }

    @Override
    public void deleteComment(Long id) {
        String currentUsername = getCurrentUsername();
        UserEntity userEntity = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

        if (!comment.getUserId().equals(userEntity.getId())) {
            throw new RuntimeException("You are not allowed to delete this comment");
        }
        commentRepository.deleteById(id);

    }
}

