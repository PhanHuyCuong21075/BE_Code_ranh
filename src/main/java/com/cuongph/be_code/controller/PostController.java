package com.cuongph.be_code.controller;

import com.cuongph.be_code.dto.request.GetPostRequest;
import com.cuongph.be_code.dto.request.PostRequest;
import com.cuongph.be_code.dto.response.ResponseData;
import com.cuongph.be_code.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseData<Object> createPost(@RequestBody PostRequest request) {
        return new ResponseData<>().success(postService.createPost(request));

    }

    @PostMapping("/get-all")
    public ResponseData<Object> getAllPosts(@RequestBody GetPostRequest request) {
        return new ResponseData<>().success(postService.getAllPosts(request));
    }

}