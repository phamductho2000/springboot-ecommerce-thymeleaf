package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.AttributeValueDTO;
import com.webecommerce.springboot.entity.AttributeEntity;
import com.webecommerce.springboot.entity.AttributeValueEntity;
import com.webecommerce.springboot.repository.AttributeValueRepository;
import com.webecommerce.springboot.service.AttributeService;
import com.webecommerce.springboot.service.AttributeValueService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AttributeValueServiceImpl implements AttributeValueService {

    @Autowired
    AttributeValueRepository attributeValueRepository;

    @Autowired
    AttributeService attributeService;

    @Autowired
    ModelMapper mapper;

    @Override
    public AttributeValueEntity findEntityById(Long id) {
//       Optional<AttributeValueEntity> ave = Optional.of(attributeValueRepository.findById(id))
//               .filter(Optional::isPresent).map(Optional::get);
        return attributeValueRepository.findById(id).orElse(null);
    }

    @Override
    public AttributeValueDTO findById(Long id) {
        Optional<AttributeValueEntity> ave= attributeValueRepository.findById(id);
        if(ave.isPresent()) {
            return mapper.map(ave.get(), AttributeValueDTO.class);
        }
        return null;
    }

    @Override
    public AttributeValueDTO save(Long attrId, String value) {
        AttributeValueEntity newAttributeValueEntity = new AttributeValueEntity();
        AttributeEntity findAttrEntity = attributeService.findEntityById(attrId);
        newAttributeValueEntity.setValue(value);
        newAttributeValueEntity.setAttributeEntity(findAttrEntity);
        return mapper.map(attributeValueRepository.save(newAttributeValueEntity), AttributeValueDTO.class);
    }

    @Override
    public AttributeValueDTO update(AttributeValueDTO attributeValueDTO) {
        AttributeValueEntity ave = findEntityById(attributeValueDTO.getId());
        ave.setValue(attributeValueDTO.getValue());
        return mapper.map(attributeValueRepository.save(ave), AttributeValueDTO.class);
    }
}
