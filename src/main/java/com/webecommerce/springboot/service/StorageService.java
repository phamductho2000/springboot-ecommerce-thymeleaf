package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.ImageDTO;
import com.webecommerce.springboot.entity.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    String storeFile(MultipartFile file);

    List<ImageDTO> loadAll(); //load all file inside a

    List<ImageDTO> loadAllFromDb();

    byte[] readFileContent(String fileName);

    void deleteAllFiles();

    ImageEntity findByIdFromDb(Long id);
}
