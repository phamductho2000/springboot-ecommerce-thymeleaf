package com.webecommerce.springboot.repository;

import com.webecommerce.springboot.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findAllByParentId(Long parentId);

    List<CategoryEntity> findAllByStatus(boolean status);

    CategoryEntity findByNameEquals(String cateName);
}
