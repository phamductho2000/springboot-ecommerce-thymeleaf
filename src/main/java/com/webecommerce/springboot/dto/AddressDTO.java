package com.webecommerce.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressDTO extends AbstractDTO {

    private String province;

    private String district;

    private String wards;

    private String deliveryAddress;
}
