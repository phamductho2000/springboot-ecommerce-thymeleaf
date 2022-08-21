package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.AttributeValueDTO;
import com.webecommerce.springboot.entity.AttributeValueEntity;

public interface AttributeValueService {

    AttributeValueEntity findEntityById(Long id);

    AttributeValueDTO findById(Long id);

    AttributeValueDTO save(Long attrId, String value);

    AttributeValueDTO update(AttributeValueDTO attributeValueDTO);
}
