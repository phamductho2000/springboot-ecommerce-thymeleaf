package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.DeliveryAddressDTO;
import com.webecommerce.springboot.entity.DeliveryAddressEntity;
import com.webecommerce.springboot.entity.UserEntity;
import com.webecommerce.springboot.repository.DeliveryAddressRepository;
import com.webecommerce.springboot.service.DeliveryAddressService;
import com.webecommerce.springboot.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    UserService userService;

    @Override
    public List<DeliveryAddressDTO> findAllByUserEmail(String email) {
        return deliveryAddressRepository.findAllByUserEntity_Email(email).stream()
                .map(a -> new DeliveryAddressDTO(
                        a.getId(),
                        a.getCustomerName(),
                        a.getCustomerPhone(),
                        a.getDeliveryAddress(),
                        a.getProvinceId(),
                        a.getProvinceName(),
                        a.getDistrictId(),
                        a.getDistrictName(),
                        a.getWardsCode(),
                        a.getWardsName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryAddressDTO> findAllByUserId(Long id) {
        return deliveryAddressRepository.findAllByUserEntity_Id(id).stream()
                .map(a -> new DeliveryAddressDTO(
                        a.getId(),
                        a.getCustomerName(),
                        a.getCustomerPhone(),
                        a.getDeliveryAddress(),
                        a.getProvinceId(),
                        a.getProvinceName(),
                        a.getDistrictId(),
                        a.getDistrictName(),
                        a.getWardsCode(),
                        a.getWardsName()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryAddressDTO findById(Long id) {
        Optional<DeliveryAddressEntity> deliveryAddressEntity = deliveryAddressRepository.findById(id);
        if(deliveryAddressEntity.isPresent()) {
            return mapper.map(deliveryAddressEntity.get(), DeliveryAddressDTO.class);
        }
        return null;
    }

    @Override
    public DeliveryAddressEntity findEntityById(Long id) {
        Optional<DeliveryAddressEntity> deliveryAddressEntity = deliveryAddressRepository.findById(id);
        return deliveryAddressEntity.orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    @Override
    public DeliveryAddressDTO save(String userEmail, DeliveryAddressDTO deliveryAddressDTO) {
        UserEntity userEntity = userService.findByEmail(userEmail);
        DeliveryAddressEntity newDeliveryAddressEntity = mapper.map(deliveryAddressDTO, DeliveryAddressEntity.class);
        newDeliveryAddressEntity.setUserEntity(userEntity);
        return mapper.map(deliveryAddressRepository.save(newDeliveryAddressEntity), DeliveryAddressDTO.class);
    }

    @Override
    public DeliveryAddressDTO update(Long id, String userEmail, DeliveryAddressDTO deliveryAddressDTO) {
        DeliveryAddressEntity updateAddress = findEntityById(id);
        if(updateAddress != null) {
            UserEntity userEntity = userService.findByEmail(userEmail);
            updateAddress = mapper.map(deliveryAddressDTO, DeliveryAddressEntity.class);
            updateAddress.setId(id);
            updateAddress.setUserEntity(userEntity);
            return mapper.map(deliveryAddressRepository.save(updateAddress), DeliveryAddressDTO.class);
        }
        return null;
    }

}
