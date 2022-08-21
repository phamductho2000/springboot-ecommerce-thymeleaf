package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailOrderDTO extends AbstractDTO {

    private int total;

    private int quantity;

    private int price;

    private ProductDTO product;

}
