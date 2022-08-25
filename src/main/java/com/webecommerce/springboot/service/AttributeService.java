package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.AttributeAndValueDTO;
import com.webecommerce.springboot.dto.AttributeAndValueFilterDTO;
import com.webecommerce.springboot.dto.AttributeDTO;
import com.webecommerce.springboot.dto.AttributeValueDTO;
import com.webecommerce.springboot.entity.AttributeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AttributeService {
    List<AttributeDTO> findAll();

    Page<AttributeDTO> findAll(Pageable pageable);

    AttributeEntity findById(Long id);

    List<AttributeValueDTO> findAllAttrValueByAttrId(Long attrId);

    AttributeDTO save(AttributeDTO attributeDTO);

    List<AttributeAndValueFilterDTO> getAttrAndValueByCate(String cate);

    AttributeAndValueDTO addAttrAndValue(String attr, String value);

    List<AttributeAndValueFilterDTO> findAllByTypeProductId(Long typeId);

    void remove(Long id);
}
