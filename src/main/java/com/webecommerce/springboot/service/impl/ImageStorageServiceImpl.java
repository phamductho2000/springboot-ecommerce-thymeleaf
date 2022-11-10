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
    public List<ImageDTO> storeFile(MultipartFile file, String path) {
        try {
            path = path.replace("\\", "/");
            Path destinationFilePath = null;
            String fileName = file.getOriginalFilename();
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
            // check file exist
            if (countFileNameDuplicate(file.getOriginalFilename(), path) > 0) {
                fileName = FilenameUtils.getBaseName(file.getOriginalFilename())
                        .concat(String.format("(%d)", countFileNameDuplicate(file.getOriginalFilename(), path)));
                fileName = fileName.concat(".").concat(FilenameUtils.getExtension(file.getOriginalFilename()));
            }
            if (path == null || path.isEmpty()) {
                destinationFilePath = this.storageFolder.resolve(Paths.get(fileName)).normalize();
            } else {
                destinationFilePath = Paths.get(path).resolve(Paths.get(fileName)).normalize();
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            ImageEntity newImageEntity = new ImageEntity();
            newImageEntity.setUrl(destinationFilePath.toString().replace("\\", "/"));
            newImageEntity.setName(fileName);
            newImageEntity.setSize(fileSizeInMegabytes);
            newImageEntity.setParentPath(path);
            imageRepository.save(newImageEntity);
            return loadAllFromDbByFolder(path);
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
        int lastIndex = 0;
        if (fullPath.contains("/")) {
            lastIndex = fullPath.lastIndexOf("/");
        } else {
            lastIndex = fullPath.lastIndexOf("\\");
        }
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
            String formatFolderName = folderName;
            if(folderName.contains(" ")) {
                formatFolderName = folderName.replace(" ", "_");
            }
            String newPathFolder = parentPath.replace("\\", "/").concat("/".concat(formatFolderName));

            Path path = Paths.get(newPathFolder);

            Files.createDirectories(path);

            System.out.println("Directory is created!");

        } catch (IOException e) {
            System.err.println("Failed to create directory!" + e.getMessage());
        }
    }

    @Override
    public void editImg(String imgName, Long imgId, String imgPath) {
        String parentPath = getParentPath(imgPath)[1];
        Path originFile = Paths.get(imgPath);
        Path newFile = Paths.get(parentPath.concat("/".concat(imgName)));
        try {
            //update img local
            Files.move(originFile, newFile, StandardCopyOption.REPLACE_EXISTING);
            //update img db
            ImageEntity imageEntity = findByIdFromDb(imgId);
            imageEntity.setUrl(newFile.toString());
            imageRepository.save(imageEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ImageDTO> loadAllFromDbByFolder(String path) {
        return imageRepository.findAllByParentPath(path.replace("\\", "/")).stream()
                .map(i -> mapper.map(i, ImageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ImageDTO deleteImg(Long id, String path) {
        Path originFile = Paths.get(path);
        try {
            Files.delete(originFile);
            Optional<ImageEntity> image = imageRepository.findById(id);
            if (image.isPresent()) {
                imageRepository.delete(image.get());
            }
            return mapper.map(image, ImageDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ImageDTO moveImg(Long id, String oldPath, String newPath) {
        String fileName = getParentPath(oldPath)[0];
        Path destinationFilePath = Paths.get(newPath).resolve(Paths.get(fileName)).normalize();
        try {
            Files.move(Paths.get(oldPath).normalize(), destinationFilePath);
            Optional<ImageEntity> image = imageRepository.findById(id);
            if (image.isPresent()) {
                image.get().setParentPath(newPath.replace("\\", "/"));
                image.get().setUrl(destinationFilePath.toString().replace("\\", "/"));
                return mapper.map(imageRepository.save(image.get()), ImageDTO.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long countFileNameDuplicate(String fileName, String parentPath) {
        return imageRepository.countAllByNameEqualsAndParentPathEquals(fileName, parentPath);
    }
}
