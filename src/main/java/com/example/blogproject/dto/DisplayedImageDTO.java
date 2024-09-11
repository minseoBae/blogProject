package com.example.blogproject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DisplayedImageDTO {
    private String originalName; // Original name of the image file
    private String savedPath;    // Path where the image file is saved
}