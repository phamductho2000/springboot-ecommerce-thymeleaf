package com.webecommerce.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FormSaveAddressDTO extends AbstractDTO {

    private Long id;

    private Long provinceId;

    private Long districtId;

    private Long wardsId;

    private String deliveryAddress;
}
