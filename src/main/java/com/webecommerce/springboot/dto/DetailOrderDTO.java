package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailOrderDTO extends AbstractDTO {

    private Long id;

    private long total;

    private int quantity;

    private long price;

    private ProductDTO product;

}
