package com.example.blogproject.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final String uploadDir = "C://Temp/upload/";

    public String storeFile(MultipartFile file) throws IOException {
        // 파일 저장 디렉토리 경로 설정
        Path uploadPath = Paths.get(uploadDir);

        // 저장할 파일의 경로 설정
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.contains("..")) {
            throw new IOException("Invalid file name: " + fileName);
        }

        Path filePath = uploadPath.resolve(fileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath);

        return fileName;
    }
}