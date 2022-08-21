package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryDTO extends AbstractDTO {

    private String name;

    private boolean status;

    private Integer level;

    private String slug;

    private Long parentId;
}
