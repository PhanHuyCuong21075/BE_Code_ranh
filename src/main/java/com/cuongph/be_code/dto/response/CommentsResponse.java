package com.cuongph.be_code.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class CommentsResponse {
    private Long id;
    private String userComment;
    private String content;
    private String imageUrl;
    private LocalDateTime time;
}
