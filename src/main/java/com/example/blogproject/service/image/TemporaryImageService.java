package com.example.blogproject.service.image;

import com.example.blogproject.dto.DisplayedImageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TemporaryImageService {
    @Value("${temporaryLocation}")
    private String temporaryLocation;

    private final FileService fileService;

    public DisplayedImageDTO saveTemporaryImage(MultipartFile imgFile) throws IOException {
        String originalName = imgFile.getOriginalFilename();
        String imgName = fileService.uploadFile(temporaryLocation, originalName, imgFile.getBytes());
        String imgUrl = "/image/temporary/" + imgName;

        return DisplayedImageDTO.builder()
                .originalName(originalName)
                .savedPath(imgUrl)
                .build();
    }
}