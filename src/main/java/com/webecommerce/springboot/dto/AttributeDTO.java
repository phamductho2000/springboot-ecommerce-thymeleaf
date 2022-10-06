package com.webecommerce.springboot.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AttributeDTO {

    private Long id;

    private String name;

    private String code;

    private List<AttributeValueDTO> attributeValueEntities;

}
