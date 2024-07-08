package com.example.blogproject.security.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {
    @NotEmpty
    private String username;
    @NotEmpty
//    @Pattern(regexp=  "^(?=.[a-zA-Z])(?=.\d)(?=.*\W).{8,20}$") 8~20 사이 영어 특수문자 포함
    private String password;
}