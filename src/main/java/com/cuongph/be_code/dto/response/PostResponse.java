package com.cuongph.be_code.dto.response;

import lombok.Data;

@Data
public class PostResponse {
    private Long id;
    private String author;
    private String content;
    private String time;
    private Long isPublic;
}