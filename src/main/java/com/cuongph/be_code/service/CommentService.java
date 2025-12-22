package com.cuongph.be_code.service;

import com.cuongph.be_code.dto.request.CommentCreateRequest;
import com.cuongph.be_code.dto.response.CommentsResponse;
import com.cuongph.be_code.entity.CommentEntity;

import java.util.List;

public interface CommentService {

    CommentEntity createOrUpdateComment(CommentCreateRequest request);

    List<CommentsResponse> commentsInPost(Long id);

      void deleteComment(Long id);
}
