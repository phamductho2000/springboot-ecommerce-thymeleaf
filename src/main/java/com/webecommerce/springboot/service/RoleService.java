package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.RoleDTO;
import com.webecommerce.springboot.entity.RoleEntity;

import java.util.List;

public interface RoleService {

    List<RoleDTO> findAllRole();

    List<RoleDTO> findAllRoleAdmin();

    List<RoleDTO> findAllRoleByUsername(String username);

    RoleEntity findEntityById(Long id);

    RoleDTO findById(Long id);

    RoleEntity save(RoleDTO roleDTO, Long[] perIds);

    void remove(Long id);
}
