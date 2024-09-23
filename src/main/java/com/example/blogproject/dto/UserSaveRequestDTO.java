package com.example.blogproject.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSaveRequestDTO {
    private String username;
    private String password;
    private String email;
    private String name;
    private MultipartFile profileImage;
    private String blogName;
}
