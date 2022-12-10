package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProductDTO extends AbstractDTO {

    private String id;

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

    private String slugSeo;

    private long typeProduct;

    private List<ImageDTO> images;
    
    private List<AttributeAndValueDTO> attributes;

    private List<CategoryDTO> categories;

    private List<ProductDTO> groupProducts;
}
