package com.encore.board.author.dto;

import com.encore.board.author.domain.Role;
import com.encore.board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data

@Builder
@AllArgsConstructor
public class AuthorDetailResDto {
    private long id;
    private String name;
    private String email;
    private String password;
    private Role role;
    private LocalDateTime createdTime;
    private int postsNumber;
}
