package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.AttributeAndValueDTO;
import com.webecommerce.springboot.dto.AttributeAndValueFilterDTO;
import com.webecommerce.springboot.dto.AttributeDTO;
import com.webecommerce.springboot.dto.AttributeValueDTO;
import com.webecommerce.springboot.entity.AttributeEntity;
import com.webecommerce.springboot.entity.AttributeValueEntity;
import com.webecommerce.springboot.repository.AttributeRepository;
import com.webecommerce.springboot.repository.AttributeValueRepository;
import com.webecommerce.springboot.service.AttributeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    AttributeRepository attributeRepository;

    @Autowired
    AttributeValueRepository attributeValueRepository;

    @Autowired
    ModelMapper mapper;

    @Override
    public List<AttributeDTO> findAll() {
        return attributeRepository.findAll().stream()
                .map(a -> mapper.map(a, AttributeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<AttributeDTO> findAll(Pageable pageable) {
        return convertListToPage(attributeRepository.findAll().stream()
                .map(a -> mapper.map(a, AttributeDTO.class))
                .collect(Collectors.toList()), pageable);
    }

    @Override
    public AttributeEntity findById(Long id) {
        return attributeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    @Override
    public List<AttributeValueDTO> findAllAttrValueByAttrId(Long attrId) {
        return attributeRepository.findAllAttrValueByAttrId(attrId);
    }

    @Override
    public AttributeDTO save(AttributeDTO attributeDTO) {
        AttributeEntity newAttributeEntity = mapper.map(attributeDTO, AttributeEntity.class);
        return mapper.map(attributeRepository.save(newAttributeEntity), AttributeDTO.class);
    }

    @Override
    public List<AttributeAndValueFilterDTO> getAttrAndValueByCate(String cate) {
        Set<AttributeAndValueDTO> attributeAndValueDTOS = attributeRepository.getAttrAndValueByCate(cate).stream()
                .map(o -> new AttributeAndValueDTO(new AttributeDTO((Long) o[0], (String) o[1]), null)
                ).collect(Collectors.toSet());
        return attributeAndValueDTOS.stream().map(s ->
                new AttributeAndValueFilterDTO(s.getAttr(), findAllAttrValueByAttrId(s.getAttr().getId())))
                .collect(Collectors.toList());
    }

    @Override
    public AttributeAndValueDTO addAttrAndValue(String attr, String value) {
        AttributeEntity newAttr = new AttributeEntity();
        newAttr.setName(attr);
        newAttr = attributeRepository.save(newAttr);
        AttributeValueEntity newValue = new AttributeValueEntity();
        newValue.setValue(value);
        newValue.setAttributeEntity(newAttr);
        newValue = attributeValueRepository.save(newValue);
        return new AttributeAndValueDTO(
                mapper.map(newAttr,AttributeDTO.class),
                mapper.map(newValue, AttributeValueDTO.class)
        );
    }

    @Override
    public void remove(Long id) {
        attributeRepository.deleteById(id);
    }

    public <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = ((start + pageable.getPageSize()) > list.size() ? list.size()
                : (start + pageable.getPageSize()));
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
}
