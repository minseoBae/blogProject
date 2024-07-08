package com.example.blogproject.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserLoginResponse {
    private String accessToken;
    private String refreshToken;
    private Long userId;
    private String name;
}
