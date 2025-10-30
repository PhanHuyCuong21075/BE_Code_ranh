package com.cuongph.be_code.service.impl;

import com.cuongph.be_code.dto.request.GetPostRequest;
import com.cuongph.be_code.dto.request.PostRequest;
import com.cuongph.be_code.dto.response.PostResponse;
import com.cuongph.be_code.dto.response.ResponseData;
import com.cuongph.be_code.entity.Friend;
import com.cuongph.be_code.entity.Post;
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
    public Post createPost(PostRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Not found"));

        Post post = new Post();
        post.setUser(user);
        post.setContent(request.getContent());
        post.setImageUrl(request.getImageUrl());
        post.setIsPublic(request.getIsPublic());
        return postRepository.save(post);
    }

    @Override
    public List<PostResponse> getAllPosts(GetPostRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Friend> friends = friendRepository.findAcceptedFriends(user.getId());

        List<Long> friendIds = new ArrayList<>();
        if (friends != null && !friends.isEmpty()) {
            for (Friend f : friends) {
                if (!f.getRequester().getId().equals(user.getId())) {
                    friendIds.add(f.getRequester().getId());
                }
                if (!f.getReceiver().getId().equals(user.getId())) {
                    friendIds.add(f.getReceiver().getId());
                }
            }
        }

        List<Post> posts;

        if (!friendIds.isEmpty()) {
            // ✅ Có bạn bè → Lấy bài của user + bạn bè (ưu tiên công khai hoặc chính họ)
            posts = postRepository.findPostsByUserIds(friendIds, user.getId());
        } else {
            // ✅ Không có bạn bè → Lấy bài công khai + bài của chính mình
            posts = postRepository.findAllPublicPostsAndUser(user.getId());
        }

        // ✅ Chuyển sang DTO + sắp xếp mới nhất đến cũ
        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(this::convertToResponse)
                .toList();
    }

    public Post updatePost(Long id, PostRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Lấy username của người đang đăng nhập
        String currentUsername = auth.getName();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        post.setUser(user);
        post.setContent(request.getContent());
        post.setIsPublic(request.getIsPublic());
        post.setUpdateAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Lấy username của người đang đăng nhập
        String currentUsername = auth.getName();

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));
        // Kiểm tra username người đăng vs username bài viết
        if (!post.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("Khong the xoa");
        }
        postRepository.delete(post);
    }


    private PostResponse convertToResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setAuthor(post.getUser().getUsername());
        response.setContent(post.getContent());
        response.setIsPublic(post.getIsPublic());
        response.setTime(post.getCreatedAt().toString());
        return response;
    }


}
