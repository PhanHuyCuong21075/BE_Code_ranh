package com.cuongph.be_code.service.impl;

import com.cuongph.be_code.dto.request.GetPostRequest;
import com.cuongph.be_code.dto.request.PostRequest;
import com.cuongph.be_code.dto.response.PostResponse;
import com.cuongph.be_code.entity.FriendEntity;
import com.cuongph.be_code.entity.PostEntity;
import com.cuongph.be_code.entity.User;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostEntity createPost(PostRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        PostEntity postEntity = new PostEntity();
        postEntity.setUser(user);
        postEntity.setContent(request.getContent());
        postEntity.setImageUrl(request.getImageUrl());
        postEntity.setIsPublic(request.getIsPublic());
        return postRepository.save(postEntity);
    }

    @Override
    public List<PostResponse> getAllPosts(GetPostRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<FriendEntity> friendEntities = friendRepository.findAcceptedFriends(user.getId());

        List<Long> friendIds = new ArrayList<>();
        if (friendEntities != null && !friendEntities.isEmpty()) {
            for (FriendEntity f : friendEntities) {
                if (!f.getRequester().getId().equals(user.getId())) {
                    friendIds.add(f.getRequester().getId());
                }
                if (!f.getReceiver().getId().equals(user.getId())) {
                    friendIds.add(f.getReceiver().getId());
                }
            }
        }

        List<PostEntity> postEntities;

        if (!friendIds.isEmpty()) {
            // ✅ Có bạn bè → Lấy bài của user + bạn bè (ưu tiên công khai hoặc chính họ)
            postEntities = postRepository.findPostsByUserIds(friendIds, user.getId());
        } else {
            // ✅ Không có bạn bè → Lấy bài công khai + bài của chính mình
            postEntities = postRepository.findAllPublicPostsAndUser(user.getId());
        }

        // ✅ Chuyển sang DTO + sắp xếp mới nhất đến cũ
        return postEntities.stream()
                .sorted(Comparator.comparing(PostEntity::getCreatedAt).reversed())
                .map(this::convertToResponse)
                .toList();
    }

    public PostEntity updatePost(Long id, PostRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Lấy username của người đang đăng nhập
        String currentUsername = auth.getName();

        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        postEntity.setUser(user);
        postEntity.setContent(request.getContent());
        postEntity.setIsPublic(request.getIsPublic());
        postEntity.setUpdateAt(LocalDateTime.now());
        return postRepository.save(postEntity);
    }

    public void deletePost(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Lấy username của người đang đăng nhập
        String currentUsername = auth.getName();

        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));
        // Kiểm tra username người đăng vs username bài viết
        if (!postEntity.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("Khong the xoa");
        }
        postRepository.delete(postEntity);
    }


    private PostResponse convertToResponse(PostEntity postEntity) {
        PostResponse response = new PostResponse();
        response.setId(postEntity.getId());
        response.setAuthor(postEntity.getUser().getUsername());
        response.setContent(postEntity.getContent());
        response.setIsPublic(postEntity.getIsPublic());
        response.setTime(postEntity.getCreatedAt().toString());
        return response;
    }


}
