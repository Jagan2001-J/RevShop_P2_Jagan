package com.rev.app.service.Impl;

import com.rev.app.service.Interface.IFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements IFileStorageService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public String saveFile(MultipartFile file) {
        try {
            Path root = Paths.get(uploadDir);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = root.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            log.info("File saved successfully: {}", filename);
            return "/uploads/" + filename;
        } catch (IOException e) {
            log.error("Could not save file: {}", e.getMessage());
            throw new RuntimeException("Could not save file: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            String actualFileName = fileName.replace("/uploads/", "");
            Path filePath = Paths.get(uploadDir).resolve(actualFileName);
            Files.deleteIfExists(filePath);
            log.info("File deleted successfully: {}", actualFileName);
        } catch (IOException e) {
            log.error("Could not delete file: {}", e.getMessage());
        }
    }
}
