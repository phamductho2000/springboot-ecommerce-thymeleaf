package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FormAddOrderDTO {

    private Long orderId;

    private Long cusId;

    private Long deliveryAddressId;

    private List<DetailOrderDTO> detailOrders;

    private Double total;

    private Double feeShip;

    private int status;
}
