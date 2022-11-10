package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.ImageDTO;
import com.webecommerce.springboot.dto.UploadFoldersDTO;
import com.webecommerce.springboot.entity.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageService {
    List<ImageDTO> storeFile(MultipartFile file, String path);

    List<ImageDTO> loadAll(); //load all file inside a

    List<ImageDTO> loadAllFromDb();

    byte[] readFileContent(String fileName);

    void deleteAllFiles();

    ImageEntity findByIdFromDb(Long id);

    List<UploadFoldersDTO> loadAllFolders() throws IOException;

    void createSubFolder(String parentPath, String folderName);

    void editImg(String imgName, Long imgId, String imgPath);

    List<ImageDTO> loadAllFromDbByFolder(String path);

    ImageDTO deleteImg(Long id, String path);

    ImageDTO moveImg(Long id, String oldPath, String newPath);
}
