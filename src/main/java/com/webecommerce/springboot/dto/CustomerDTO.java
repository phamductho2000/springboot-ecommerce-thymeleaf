package com.webecommerce.springboot.dto;

import com.webecommerce.springboot.entity.DeliveryAddressEntity;
import com.webecommerce.springboot.entity.OrderEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomerDTO extends AbstractDTO {

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

    private List<OrderDTO> orders;

    private List<DeliveryAddressDTO> deliveryAddresses;
}
