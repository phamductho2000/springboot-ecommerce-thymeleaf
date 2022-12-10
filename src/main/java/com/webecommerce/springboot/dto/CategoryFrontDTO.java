package com.webecommerce.springboot.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CategoryFrontDTO extends AbstractDTO {
    private Long id;

    private String name;

    private Long parentId;

    private String slug;

    private List<CategoryFrontDTO> children;
}
