package com.cuongph.be_code.service;

import com.cuongph.be_code.dto.request.GetPostRequest;
import com.cuongph.be_code.dto.request.PostRequest;
import com.cuongph.be_code.dto.response.PostResponse;
import com.cuongph.be_code.entity.PostEntity;

import java.util.List;

public interface PostService {

    PostEntity createPost(PostRequest request);

    List<PostResponse> getAllPosts(GetPostRequest request);

    PostEntity updatePost(Long id, PostRequest request);

    void deletePost(Long id);
}
