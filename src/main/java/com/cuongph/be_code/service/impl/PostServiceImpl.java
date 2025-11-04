package com.cuongph.be_code.service.impl;

import com.cuongph.be_code.dto.request.GetPostRequest;
import com.cuongph.be_code.dto.request.PostRequest;
import com.cuongph.be_code.dto.response.PostResponse;
import com.cuongph.be_code.entity.FriendEntity;
import com.cuongph.be_code.entity.PostEntity;
import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.repo.FriendRepository;
import com.cuongph.be_code.repo.PostRepository;
import com.cuongph.be_code.repo.UserRepository;
import com.cuongph.be_code.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Tạo bài viết mới
     */
    @Override
    public PostEntity createPost(PostRequest request) {
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
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

        // 🔹 Lấy danh sách bạn bè đã chấp nhận
        List<FriendEntity> friendEntities = friendRepository.findAcceptedFriends(userEntity.getId());

        // 🔹 Lấy danh sách ID bạn bè
        List<Long> friendIds = friendEntities.stream()
                .map(f -> f.getRequesterId().equals(userEntity.getId())
                        ? f.getReceiverId()
                        : f.getRequesterId())
                .distinct()
                .collect(Collectors.toList());

        List<PostEntity> postEntities;

        if (!friendIds.isEmpty()) {
            // ✅ Có bạn bè → Lấy bài của user + bạn bè (ưu tiên công khai hoặc chính họ)
            postEntities = postRepository.findPostsByUserIds(friendIds, userEntity.getId());
        } else {
            // ✅ Không có bạn bè → Lấy bài công khai + bài của chính mình
            postEntities = postRepository.findAllPublicPostsAndUser(userEntity.getId());
        }

        // ✅ Map từ PostEntity → PostResponse
        return postEntities.stream()
                .sorted(Comparator.comparing(PostEntity::getCreatedAt).reversed())
                .map(this::convertToResponse)
                .toList();
    }

    /**
     * Cập nhật bài viết (chỉ chủ sở hữu mới được sửa)
     */
    public PostEntity updatePost(Long id, PostRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

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
    public void deletePost(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        UserEntity userEntity = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

        if (!postEntity.getUserId().equals(userEntity.getId())) {
            throw new RuntimeException("Không thể xoá bài viết của người khác");
        }

        postRepository.delete(postEntity);
    }

    /**
     * Convert Entity → Response
     */
    private PostResponse convertToResponse(PostEntity postEntity) {
        PostResponse response = new PostResponse();
        response.setId(postEntity.getId());
        response.setContent(postEntity.getContent());
        response.setIsPublic(postEntity.getIsPublic());
        response.setTime(postEntity.getCreatedAt().toString());

        // 🔹 Lấy tên người đăng từ userId
        userRepository.findById(postEntity.getUserId())
                .ifPresent(user -> response.setAuthor(user.getUsername()));

        return response;
    }
}
