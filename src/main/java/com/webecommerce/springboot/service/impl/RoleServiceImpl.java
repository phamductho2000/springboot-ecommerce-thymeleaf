package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.RoleDTO;
import com.webecommerce.springboot.entity.RoleEntity;
import com.webecommerce.springboot.repository.RoleRepository;
import com.webecommerce.springboot.service.PermissionService;
import com.webecommerce.springboot.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionService permissionService;

    @Autowired
    ModelMapper mapper;

    @Override
    public List<RoleDTO> findAllRole() {
        return roleRepository.findAll().stream()
                .map(r -> mapper.map(r, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDTO> findAllRoleAdmin() {
        return roleRepository.findAllByCode("ADMIN").stream()
                .map(r -> mapper.map(r, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RoleDTO> findAllRoleByUsername(String username) {
        List<RoleEntity> roleEntities = roleRepository.findAllByUsers_Username(username);
        List<RoleDTO> roleDTOS = new ArrayList<>();
//        for(RoleEntity item: roleEntities) {
//            RoleDTO roleDTO = roleConverter.entityToDTO(item);
//            roleDTOS.add(roleDTO);
//        }
        return roleDTOS;
    }

    @Override
    public RoleEntity findEntityById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    @Override
    public RoleDTO findById(Long id) {
        Optional<RoleEntity> r = roleRepository.findById(id);
        if(r.isPresent()) {
            return mapper.map(r.get(), RoleDTO.class);
        }
        return null;
    }

    @Override
    public RoleEntity save(RoleDTO roleDTO, Long[] perIds) {
        if(roleDTO.getId() != 0) {
            RoleEntity updateRole = findEntityById(roleDTO.getId());
            updateRole = mapper.map(roleDTO, RoleEntity.class);
            updateRole.setPermissions(
                    Arrays.stream(perIds).map(id -> permissionService.findEntityById(id))
                            .collect(Collectors.toList())
            );
            updateRole.setCode("ADMIN");
            return roleRepository.save(updateRole);
        }
        RoleEntity newRole = mapper.map(roleDTO, RoleEntity.class);
        newRole.setPermissions(
                Arrays.stream(perIds).map(id -> permissionService.findEntityById(id))
                .collect(Collectors.toList())
        );
        newRole.setCode("ADMIN");
        return roleRepository.save(newRole);
    }

    @Override
    public void remove(Long id) {
        roleRepository.deleteById(id);
    }
}
