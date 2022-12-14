package com.webecommerce.springboot.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TypeProductDTO extends AbstractDTO {

    private Long id;

    private String name;

    List<AttributeDTO> attributes;
}
