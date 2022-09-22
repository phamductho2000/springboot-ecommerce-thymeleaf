package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.AttributeDTO;
import com.webecommerce.springboot.dto.TypeProductDTO;
import com.webecommerce.springboot.entity.AttributeEntity;
import com.webecommerce.springboot.entity.TypeProductEntity;
import com.webecommerce.springboot.repository.AttributeRepository;
import com.webecommerce.springboot.repository.TypeProductRepository;
import com.webecommerce.springboot.service.AttributeService;
import com.webecommerce.springboot.service.TypeProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TypeProductServiceImpl implements TypeProductService {

    @Autowired
    AttributeService attributeService;

    @Autowired
    TypeProductRepository typeProductRepository;

    @Autowired
    AttributeRepository attributeRepository;

    @Autowired
    ModelMapper mapper;

    @Override
    public List<TypeProductDTO> findAll() {
        return typeProductRepository.findAll().stream()
                .map(entity -> mapper.map(entity, TypeProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public TypeProductEntity save(TypeProductDTO typeProductDTO) {
        TypeProductEntity newEntity = mapper.map(typeProductDTO, TypeProductEntity.class);
        newEntity = typeProductRepository.save(newEntity);
        for (AttributeDTO attributeDTO :  typeProductDTO.getAttributes()) {
            AttributeEntity attributeEntity = attributeService.findEntityById(attributeDTO.getId());
            attributeEntity.setTypeProductEntity(newEntity);
            attributeRepository.save(attributeEntity);
        }
        return newEntity;
    }

    @Override
    public TypeProductDTO findById(Long id) {
       Optional<TypeProductEntity> typeProductEntity = typeProductRepository.findById(id);
       if(typeProductEntity.isPresent()) {
           TypeProductDTO typeProductDTO = mapper.map(typeProductEntity, TypeProductDTO.class);
           List<AttributeDTO> attributeDTOS = typeProductEntity.get().getAttributeEntities().stream()
                   .map(attrEntity -> mapper.map(attributeService.findEntityById(attrEntity.getId()), AttributeDTO.class))
                   .collect(Collectors.toList());
           typeProductDTO.setAttributes(attributeDTOS);
           return typeProductDTO;
       }
       return null;
    }
}
