package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findAllByParentPath(String path);

    Long countAllByNameEqualsAndParentPathEquals(String name, String parentPath);
}
