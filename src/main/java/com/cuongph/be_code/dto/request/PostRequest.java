package com.cuongph.be_code.dto.request;

import lombok.Data;

@Data
public class PostRequest {
    private String username;
    private String content;
    private String imageUrl;
    private Long isPublic;
}