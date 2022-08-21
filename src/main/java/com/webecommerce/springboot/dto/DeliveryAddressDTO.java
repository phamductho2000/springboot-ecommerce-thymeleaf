package com.webecommerce.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddressDTO {

    private Long id;

    private String customerName;

    private String customerPhone;

    private String deliveryAddress;

    private Long provinceId;

    private String provinceName;

    private Long districtId;

    private String districtName;

    private String wardsCode;

    private String wardsName;
}
