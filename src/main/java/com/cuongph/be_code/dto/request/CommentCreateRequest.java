package com.cuongph.be_code.dto.request;

import lombok.Data;

@Data
public class CommentCreateRequest {
    private Long commentId;
    private Long postId;
    private String content;
    private String imageUrl;
}
