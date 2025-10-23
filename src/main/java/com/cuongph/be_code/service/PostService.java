package com.cuongph.be_code.service;

import com.cuongph.be_code.dto.request.GetPostRequest;
import com.cuongph.be_code.dto.request.PostRequest;
import com.cuongph.be_code.dto.response.PostResponse;
import com.cuongph.be_code.entity.Post;

import java.util.List;

public interface PostService {

    Post createPost(PostRequest request);


    List<PostResponse> getAllPosts(GetPostRequest request);
}
