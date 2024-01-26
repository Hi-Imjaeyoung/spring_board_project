package com.encore.board.post.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostCreateReqDto {
    private String title;
    private String Contents;
    private String email;
    private boolean appointment;
    private String appointTime;
}
