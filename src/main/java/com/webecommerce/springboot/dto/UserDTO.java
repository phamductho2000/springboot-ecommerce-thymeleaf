package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class UserDTO extends AbstractDTO {

    private String id;

    private String name;

    private String email;

    private String address;

    private int phone;

    private String username;

    private String password;

    private String avatar;

    private boolean status = Boolean.TRUE;

    private Long roleId;

    private double totalValueBuy;

    private int totalCountOrder;

    private int totalCountProduct;

    private Timestamp latestOrder;

    private List<RoleDTO> roles;
}
