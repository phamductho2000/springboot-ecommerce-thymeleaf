package com.webecommerce.springboot.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttributeAndValueDTO {

    private AttributeDTO attr;

    private AttributeValueDTO attrValue;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttributeAndValueDTO)) return false;
        AttributeAndValueDTO that = (AttributeAndValueDTO) o;
        return Objects.equals(attr, that.attr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attr);
    }
}
