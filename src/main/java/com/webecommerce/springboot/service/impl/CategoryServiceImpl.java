package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.CategoryDTO;
import com.webecommerce.springboot.dto.CategoryFrontDTO;
import com.webecommerce.springboot.entity.CategoryEntity;
import com.webecommerce.springboot.repository.CategoryRepository;
import com.webecommerce.springboot.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper mapper;

    @Override
    public List<CategoryEntity> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public List<CategoryDTO> loadCategory(List<CategoryEntity> data, Long parentId, Integer level, String parentName) {
        List<CategoryDTO> listCategories = new ArrayList<>();
        for (CategoryEntity item : data) {
            if (item.getParentId() == parentId) {
                CategoryDTO newItem = mapper.map(item, CategoryDTO.class);
                if (!parentName.equals("")) {
                    newItem.setName(parentName + " > " + newItem.getName());
                }
                newItem.setLevel(level);
                listCategories.add(newItem);
                List<CategoryDTO> child = loadCategory(data, item.getId(), level + 1, newItem.getName());
                listCategories.addAll(child);
            }
        }
        return listCategories;
    }

    public List<CategoryFrontDTO> loadAll() {
        return categoryRepository.findAllByStatus(true).stream()
                .map(c -> mapper.map(c, CategoryFrontDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryEntity findEntityById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    @Override
    public CategoryDTO findById(Long id) {
        return mapper.map(findEntityById(id), CategoryDTO.class);
    }

    @Override
    public List<CategoryFrontDTO> listWithTree() {
        List<CategoryFrontDTO> entities = loadAll();
        return entities.stream().filter(c -> c.getParentId() == 0L)
                .map((menu) -> {
                    menu.setChildren(getChildren(menu, entities));
                    return menu;
                }).collect(Collectors.toList());
    }

    private List<CategoryFrontDTO> getChildren(CategoryFrontDTO root, List<CategoryFrontDTO> all) {
        return all.stream().filter(categoryEntity ->
                categoryEntity.getParentId() == root.getId())
                .map(categoryEntity -> {
                    categoryEntity.setChildren(getChildren(categoryEntity, all));
                    return categoryEntity;
                }).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDTO> findAllByParentId(String parentCateName) {
        return categoryRepository.findAllByParentId(categoryRepository.findByNameEquals(parentCateName).getId())
                .stream().map(c -> mapper.map(c, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryEntity save(CategoryDTO categoryDTO) {
        if(categoryDTO.getId() != 0) {
            CategoryEntity updateCate = findEntityById(categoryDTO.getId());
            updateCate = mapper.map(categoryDTO,CategoryEntity.class);
            return categoryRepository.save(updateCate);
        }
        categoryDTO.setStatus(true);
        return categoryRepository.save(mapper.map(categoryDTO, CategoryEntity.class));
    }

    @Override
    public void remove(Long id) {
        categoryRepository.deleteById(id);
    }
}
