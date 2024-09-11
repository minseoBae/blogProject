package com.example.blogproject.dto;

import com.example.blogproject.entity.blog.Blog;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSaveRequestDTO {
    private String title;
    private String content;
    private MultipartFile image;
    private Boolean status;
}