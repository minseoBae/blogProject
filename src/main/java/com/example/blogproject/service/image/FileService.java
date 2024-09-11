package com.example.blogproject.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    public String storeFile(MultipartFile file, String uploadDir) throws IOException {
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

        // UUID 생성 후 파일 이름에 추가
        String uuid = UUID.randomUUID().toString();
        String newFileName = uuid + "_" + fileName;

        Path filePath = uploadPath.resolve(newFileName);

        // 파일 저장
        Files.copy(file.getInputStream(), filePath);

        return newFileName;
    }

    public String uploadFile(String uploadDir, String originalFilename, byte[] fileData) throws IOException {
        String fileName = UUID.randomUUID() + "_" + originalFilename;
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return fileName;
    }

    public void moveFile(String sourcePath, String targetPath) throws IOException {
        Path source = Paths.get(sourcePath);
        Path target = Paths.get(targetPath);

        if (!Files.exists(target.getParent())) {
            Files.createDirectories(target.getParent());
        }

        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    public static String resourcePathToSavedPath(String resourcePath) {
        return "C:/temp" + resourcePath.replace("/image", "");
    }

    public static String savedPathToResourcePath(String savedPath) {
        return "/image" + savedPath.replace("C:/temp", "");
    }
}
