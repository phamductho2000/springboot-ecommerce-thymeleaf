package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.CategoryDTO;
import com.webecommerce.springboot.dto.CategoryFrontDTO;
import com.webecommerce.springboot.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {

    List<CategoryEntity> findAll();

//    List<CategoryDTO> loadCategory(List<CategoryEntity> data, Long parentId, Integer level);

    List<CategoryDTO> loadCategory(List<CategoryEntity> data, Long parentId, Integer level, String parentName);

    CategoryEntity findEntityById(Long id);

    CategoryDTO findById(Long id);

    List<CategoryFrontDTO> listWithTree();

    List<CategoryDTO> findAllByParentId(String parentCateName);

    CategoryEntity save(CategoryDTO categoryDTO);

    void remove(Long id);
}
