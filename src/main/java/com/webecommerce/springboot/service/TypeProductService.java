package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.TypeProductDTO;
import com.webecommerce.springboot.entity.TypeProductEntity;

import java.util.List;

public interface TypeProductService {

    List<TypeProductDTO> findAll();

    TypeProductEntity save(TypeProductDTO typeProductDTO);

    TypeProductDTO findById(Long id);
}
