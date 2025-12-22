package com.cuongph.be_code.controller;

import com.cuongph.be_code.dto.request.CommentCreateRequest;
import com.cuongph.be_code.dto.response.ResponseData;
import com.cuongph.be_code.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create-or-update")
    public ResponseData<Object> createOrUpdateComment(
            @RequestBody CommentCreateRequest request
    ) {
        return new ResponseData<>().success(commentService.createOrUpdateComment(request));
    }

    @GetMapping("/comments/{id}")
    public ResponseData<Object> getCommentById(@PathVariable Long id) {
        return new ResponseData<>().success(commentService.commentsInPost(id));

    }

    @DeleteMapping("/delete/{id}")
    public ResponseData<Object> deleteCommentById(@PathVariable Long id) {
        commentService.deleteComment(id);
        return new ResponseData<>().success("Xoá Thành Công");
    }

}
