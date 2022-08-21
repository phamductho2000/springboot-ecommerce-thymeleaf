package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.UserDTO;

import java.util.List;

public interface CustomerSevice {
    List<UserDTO> findAllCustomer();
}
