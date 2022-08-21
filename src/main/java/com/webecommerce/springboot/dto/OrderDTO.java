package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderDTO extends AbstractDTO {

    private int status;

    private int payment_status;

    private String note;

    private double total;

    private double feeShip;

    private UserDTO user;

    private List<DetailOrderDTO> detailOrders;

    private DeliveryAddressDTO deliveryAddress;
}
