package com.webecommerce.springboot.service;

import com.webecommerce.springboot.dto.PermissionDTO;
import com.webecommerce.springboot.dto.RenderPermissionDTO;
import com.webecommerce.springboot.entity.PermissionEntity;

import java.util.List;

public interface PermissionService {

    List<PermissionDTO> findAllByRoleId(long roleId);

    List<PermissionDTO> findAll();

    PermissionEntity findEntityById(Long id);

//    List<PermissionDTO> findAllByTable(String code);

    List<PermissionDTO> findAllByGenName(String genName);

    List<RenderPermissionDTO> renderAll();
}
