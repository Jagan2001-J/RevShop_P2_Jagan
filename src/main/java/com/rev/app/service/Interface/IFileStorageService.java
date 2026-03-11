package com.rev.app.service.Interface;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {
    String saveFile(MultipartFile file);

    void deleteFile(String fileName);
}
