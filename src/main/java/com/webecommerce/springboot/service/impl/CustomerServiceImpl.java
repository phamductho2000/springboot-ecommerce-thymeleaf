package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.UserDTO;
import com.webecommerce.springboot.repository.UserRepository;
import com.webecommerce.springboot.service.CustomerSevice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerSevice {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper mapper;

    @Override
    public List<UserDTO> findAllCustomer() {
        return userRepository.findAllCustomer().stream()
                .map(u -> mapper.map(u, UserDTO.class))
                .collect(Collectors.toList());
    }
}
