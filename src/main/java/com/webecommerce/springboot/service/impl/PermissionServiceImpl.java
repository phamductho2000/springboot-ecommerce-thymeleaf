package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.PermissionDTO;
import com.webecommerce.springboot.dto.RenderPermissionDTO;
import com.webecommerce.springboot.entity.PermissionEntity;
import com.webecommerce.springboot.repository.PermissionRepository;
import com.webecommerce.springboot.service.PermissionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public List<PermissionDTO> findAllByRoleId(long roleId) {
        List<PermissionEntity> permissionEntities = permissionRepository.findByRoles_Id(roleId);
        List<PermissionDTO> permissionDTOS = new ArrayList<>();
//        for(PermissionEntity item: permissionEntities) {
//            PermissionDTO permissionDTO = permissionConverter.entityToDTO(item);
//            permissionDTOS.add(permissionDTO);
//        }
        return permissionDTOS;
    }

    @Override
    public List<PermissionDTO> findAll() {
        return permissionRepository.findAll().stream()
                .map(p -> mapper.map(p, PermissionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PermissionEntity findEntityById(Long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    @Override
    public List<PermissionDTO> findAllByGenName(String genName) {
        return permissionRepository.findAllByGeneralName(genName).stream()
                .map(p -> mapper.map(p, PermissionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RenderPermissionDTO> renderAll() {
        Set<String> genNames = new HashSet<>();
        findAll().stream().forEach(p -> genNames.add(p.getGeneralName()));
        return genNames.stream().map(c -> new RenderPermissionDTO(c, findAllByGenName(c)))
                .collect(Collectors.toList());
    }

}
