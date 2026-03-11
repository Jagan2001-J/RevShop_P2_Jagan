package com.rev.app.service.Impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class FileStorageServiceImplTest {

    @TempDir
    Path tempDir;

    @Test
    public void testSaveFile() throws IOException {
        FileStorageServiceImpl fileStorageService = new FileStorageServiceImpl();
        ReflectionTestUtils.setField(fileStorageService, "uploadDir", tempDir.toString());

        MockMultipartFile file = new MockMultipartFile(
                "imageFile", "test.jpg", "image/jpeg", "test image content".getBytes());

        String resultPath = fileStorageService.saveFile(file);

        assertThat(resultPath).startsWith("/uploads/");
        String fileName = resultPath.replace("/uploads/", "");
        assertThat(tempDir.resolve(fileName)).exists();
        assertThat(Files.readAllBytes(tempDir.resolve(fileName))).isEqualTo("test image content".getBytes());
    }

    @Test
    public void testDeleteFile() throws IOException {
        FileStorageServiceImpl fileStorageService = new FileStorageServiceImpl();
        ReflectionTestUtils.setField(fileStorageService, "uploadDir", tempDir.toString());

        Path testFile = tempDir.resolve("delete_me.jpg");
        Files.write(testFile, "content".getBytes());

        fileStorageService.deleteFile("/uploads/delete_me.jpg");

        assertThat(testFile).doesNotExist();
    }
}
