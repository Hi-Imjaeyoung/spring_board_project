package com.encore.board.author.dto;

import com.encore.board.author.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class AuthorListResDto {
    private long id;
    private String name;
    private String email;
    private String password;
    private String role;
}
