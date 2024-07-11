package com.example.blogproject.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LikeRequestDTO {

    private Long userId;
    private Long postId;
}