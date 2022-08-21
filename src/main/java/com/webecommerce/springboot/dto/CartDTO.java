package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {

    private ProductDTO product;

    private int quantity;

    private double totalPrice;
}
