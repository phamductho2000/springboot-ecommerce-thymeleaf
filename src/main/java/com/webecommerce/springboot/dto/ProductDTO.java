package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
@ToString
public class ProductDTO extends AbstractDTO {

    private String name;

    private String shortDes;

    private String detailDes;

    private long price;

    private long originalPrice;

    private int quantity;

    private long sellCount;

    private int status;

    private int visible;

    private String slug;

    private long typeProduct;

    private List<ImageDTO> images;
    
    private List<AttributeAndValueDTO> attributes;

    private List<CategoryDTO> categories;
}
