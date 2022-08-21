package com.webecommerce.springboot.dto;

import com.webecommerce.springboot.entity.RoleEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO extends AbstractDTO {

    private String name;

    private String email;

    private String address;

    private int phone;

    private String username;

    private String password;

    private String avatar;

    private boolean status = Boolean.TRUE;

    private Long roleId;

    private List<RoleDTO> roles;
}
