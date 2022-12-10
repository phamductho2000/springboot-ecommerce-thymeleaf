package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleDTO extends AbstractDTO {
    private Long id;

    private String name;

    private String code;

    private List<PermissionDTO> permissions;
}
