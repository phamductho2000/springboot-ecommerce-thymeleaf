package com.webecommerce.springboot.service;


import com.webecommerce.springboot.dto.RoleDTO;
import com.webecommerce.springboot.dto.UserDTO;
import com.webecommerce.springboot.entity.AuthenticationProvider;
import com.webecommerce.springboot.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface UserService {
    List<UserDTO> findAll(Pageable pageable);

    List<RoleDTO> findAllRole();

//    @PreAuthorize("hasPermission('UserEntity', 'READ')")
    List<UserDTO> findAll();

    List<UserDTO> findAllAccount();

    UserDTO findById(Long id);

    UserEntity findEntityById(Long id);

    UserDTO findOneByUsernameAndStatus(String username, boolean status);

    UserEntity findByEmail(String email);

    UserEntity createNewUserAfterOAuth2LoginSuccess(String email, String name, AuthenticationProvider provider);

    UserEntity updateUserAfterOAuth2LoginSuccess(UserEntity userEntity, String name, AuthenticationProvider provider);

    UserEntity save(UserDTO userDTO);

    UserEntity update(UserDTO userDTO);

    void registerUser(String user, String pass, String email);

    void remove(Long id);
}