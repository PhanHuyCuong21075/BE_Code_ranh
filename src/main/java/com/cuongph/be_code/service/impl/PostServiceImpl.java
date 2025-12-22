package com.cuongph.be_code.service.impl;

import com.cuongph.be_code.common.auth.AuthService;
import com.cuongph.be_code.dto.request.GetPostRequest;
import com.cuongph.be_code.dto.request.PostRequest;
import com.cuongph.be_code.dto.response.CommentsResponse;
import com.cuongph.be_code.dto.response.PostResponse;
import com.cuongph.be_code.dto.userCurrent.UserInfoModel;
import com.cuongph.be_code.entity.CommentEntity;
import com.cuongph.be_code.entity.FriendEntity;
import com.cuongph.be_code.entity.PostEntity;
import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.repo.CommentRepository;
import com.cuongph.be_code.repo.FriendRepository;
import com.cuongph.be_code.repo.PostRepository;
import com.cuongph.be_code.repo.UserRepository;
import com.cuongph.be_code.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.cuongph.be_code.common.auth.AuthService.getCurrentUsername;


@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    /**
     * Tạo bài viết mới
     */
    @Override
    public PostEntity createPost(PostRequest request) {
        String currentUsername = getCurrentUsername();

        UserEntity userEntity = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        PostEntity postEntity = new PostEntity();
        postEntity.setUserId(userEntity.getId());
        postEntity.setContent(request.getContent());
        postEntity.setImageUrl(request.getImageUrl());
        postEntity.setIsPublic(request.getIsPublic());
        return postRepository.save(postEntity);
    }

    /**
     * Lấy danh sách bài viết hiển thị trên bảng tin
     */
    @Override
    public List<PostResponse> getAllPosts(GetPostRequest request) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<FriendEntity> friendEntities = friendRepository.findAcceptedFriends(userEntity.getId());

        List<Long> friendIds = friendEntities.stream()
                .map(f -> f.getRequesterId().equals(userEntity.getId())
                        ? f.getReceiverId()
                        : f.getRequesterId())
                .distinct()
                .collect(Collectors.toList());

        List<PostEntity> postEntities;

        if (!friendIds.isEmpty()) {
            postEntities = postRepository.findPostsByUserIds(friendIds, userEntity.getId());
        } else {
            postEntities = postRepository.findAllPublicPostsAndUser(userEntity.getId());
        }

        // ✅ Map từ PostEntity → PostResponse
        return postEntities.stream()
                .sorted(Comparator.comparing(PostEntity::getCreatedAt).reversed())
                .map(this::convertToResponse)
                .toList();
    }

    public PostEntity updatePost(Long id, PostRequest request) {
        String currentUsername = getCurrentUsername();

        UserEntity userEntity = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

        if (!postEntity.getUserId().equals(userEntity.getId())) {
            throw new RuntimeException("Không thể sửa bài viết của người khác");
        }

        postEntity.setContent(request.getContent());
        postEntity.setIsPublic(request.getIsPublic());
        postEntity.setUpdateAt(LocalDateTime.now());
        return postRepository.save(postEntity);
    }

    /**
     * Xoá bài viết (chỉ chủ sở hữu mới được xoá)
     */
    @Transactional
    public void deletePost(Long id) {
        String currentUsername = getCurrentUsername();
        UserEntity userEntity = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

        if (!postEntity.getUserId().equals(userEntity.getId())) {
            throw new RuntimeException("Không thể xoá bài viết của người khác");
        }

        List<CommentsResponse> commentEntityList = commentRepository.findCommentsByPostId(postEntity.getId());
        commentEntityList.forEach(comment -> {
            commentRepository.deleteById(comment.getId());
        });

        postRepository.delete(postEntity);
    }

    /**
     * Convert Entity → Response@!@!!@@!@@!#$#@!
     */
    private PostResponse convertToResponse(PostEntity postEntity) {
        PostResponse response = new PostResponse();
        response.setId(postEntity.getId());
        response.setContent(postEntity.getContent());
        response.setIsPublic(postEntity.getIsPublic());
        response.setTime(postEntity.getCreatedAt());

        // 🔹 Lấy tên người đăng từ userId
        userRepository.findById(postEntity.getUserId())
                .ifPresent(user -> response.setAuthor(user.getUsername()));

        return response;
    }
}
