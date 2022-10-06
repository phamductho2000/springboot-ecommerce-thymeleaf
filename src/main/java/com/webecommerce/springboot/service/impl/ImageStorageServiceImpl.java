package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.ImageDTO;
import com.webecommerce.springboot.dto.UploadFoldersDTO;
import com.webecommerce.springboot.entity.ImageEntity;
import com.webecommerce.springboot.repository.ImageRepository;
import com.webecommerce.springboot.service.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ImageStorageServiceImpl implements StorageService {
    private final Path storageFolder = Paths.get("uploads");

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ModelMapper mapper;

    public ImageStorageServiceImpl() {
        try {
            Files.createDirectories(storageFolder);
        } catch (IOException exception) {
            throw new RuntimeException("Cannot initialize storage", exception);
        }
    }

    private boolean isImageFile(MultipartFile file) {
        //Let install FileNameUtils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }

    @Override
    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }
            //check file is image ?
            if (!isImageFile(file)) {
                throw new RuntimeException("You can only upload image file");
            }
            //file must be <= 5Mb
            double fileSizeInMegabytes = file.getSize() / 1_000_000.0D;
            if (fileSizeInMegabytes > 5.0f) {
                throw new RuntimeException("File must be <= 5Mb");
            }
            //File must be rename, why ?
//            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
//            String generatedFileName = UUID.randomUUID().toString().replace("-", "");
//            generatedFileName = generatedFileName + "." + fileExtension;
            Path destinationFilePath = this.storageFolder.resolve(
                    Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
                throw new RuntimeException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            ImageEntity newImageEntity = new ImageEntity();
            newImageEntity.setUrl("/uploads/" + file.getOriginalFilename());
            newImageEntity.setName(file.getOriginalFilename());
            newImageEntity.setSize(fileSizeInMegabytes);
            imageRepository.save(newImageEntity);
            return file.getOriginalFilename();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to store file.", exception);
        }
    }

    @Override
    public List<ImageDTO> loadAll() {
        try {
            Stream<Path> paths = Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);

            return paths.map(p -> {
                ImageDTO imageDTO = null;
                try {
                    imageDTO = new ImageDTO(p.toString(), p.toString(), Files.size(storageFolder.resolve(p)) / 1_024.0D);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return imageDTO;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load stored files", e);
        }

    }

    @Override
    public List<ImageDTO> loadAllFromDb() {
        return imageRepository.findAll().stream()
                .map(i -> mapper.map(i, ImageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            } else {
                throw new RuntimeException(
                        "Could not read file: " + fileName);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Could not read file: " + fileName, exception);
        }
    }

    @Override
    public void deleteAllFiles() {

    }

    @Override
    public ImageEntity findByIdFromDb(Long id) {
        Optional<ImageEntity> imageEntity = imageRepository.findById(id);
        if (imageEntity.isPresent()) {
            return imageEntity.get();
        }
        return null;
    }

    @Override
    public List<UploadFoldersDTO> loadAllFolders() throws IOException {
        List<UploadFoldersDTO> uploadFoldersDTOs = Files.walk(storageFolder, FileVisitOption.FOLLOW_LINKS)
                .filter(Files::isDirectory)
                .map(path -> {
                    UploadFoldersDTO folder = new UploadFoldersDTO();
                    String fullPath = path.toString();
                    String[] pathFolder = getParentPath(fullPath);
                    folder.setPath(fullPath);
                    folder.setName(pathFolder[0]);
                    folder.setParentPath(pathFolder[1]);
                    return folder;
                })
                .collect(Collectors.toList());

        return uploadFoldersDTOs.stream().filter(folder -> folder.getParentPath().isEmpty())
                .map(folder -> {
                    folder.setChildren(getSubFolder(uploadFoldersDTOs, folder));
                    return folder;
                })
                .collect(Collectors.toList()).get(0).getChildren();
    }

    public List<UploadFoldersDTO> getSubFolder(List<UploadFoldersDTO> folders, UploadFoldersDTO root) {
        return folders.stream()
                .filter(folder -> folder.getParentPath().equals(root.getPath()))
                .map(folder -> {
                    folder.setChildren(getSubFolder(folders, folder));
                    return folder;
                })
                .collect(Collectors.toList());
    }

    public String[] getParentPath(String fullPath) {
        int lastIndex = fullPath.lastIndexOf("\\");
        if (lastIndex != -1) {
            String folderName = fullPath.substring(lastIndex + 1, fullPath.length());
            String parenPath = fullPath.substring(0, lastIndex);
            return new String[]{folderName, parenPath};
        }
        return new String[]{fullPath, ""};
    }

    @Override
    public void createSubFolder(String parentPath, String folderName) {
        try {
            String newPathFolder = parentPath.replace("\\", "/").concat("/".concat(folderName));

            Path path = Paths.get(newPathFolder);

            Files.createDirectories(path);

            System.out.println("Directory is created!");

        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
    }
}
