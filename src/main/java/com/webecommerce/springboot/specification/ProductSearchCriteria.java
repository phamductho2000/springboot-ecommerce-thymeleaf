package com.webecommerce.springboot.specification;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductSearchCriteria {
    private String field;
    private String operator;
    private String value;
}
