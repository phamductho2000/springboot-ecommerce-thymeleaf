package com.webecommerce.springboot.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttributeAndValueFilterDTO {

    private AttributeDTO attr;

    private List<AttributeValueDTO> attrValues;


}
