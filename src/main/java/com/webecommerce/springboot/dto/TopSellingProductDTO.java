package com.webecommerce.springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopSellingProductDTO {

    private Long productId;

    private String name;

    private long quantity;

    private double price;

    private double total;
}
