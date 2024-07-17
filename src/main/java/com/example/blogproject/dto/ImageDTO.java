package com.example.blogproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDTO {
    private String fileName;
    private String uuid;
    private String fileUrl;
}
