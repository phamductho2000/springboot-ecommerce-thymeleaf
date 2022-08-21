package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.DeliveryAddressDTO;
import com.webecommerce.springboot.entity.DeliveryAddressEntity;

import java.util.List;

public interface DeliveryAddressService {
    List<DeliveryAddressDTO> findAllByUserEmail(String email);

    List<DeliveryAddressDTO> findAllByUserId(Long id);

    DeliveryAddressDTO findById(Long id);

    DeliveryAddressEntity findEntityById(Long id);

    DeliveryAddressDTO save(String userEmail, DeliveryAddressDTO deliveryAddressDTO);

    DeliveryAddressDTO update(Long id, String userEmail, DeliveryAddressDTO deliveryAddressDTO);
}
