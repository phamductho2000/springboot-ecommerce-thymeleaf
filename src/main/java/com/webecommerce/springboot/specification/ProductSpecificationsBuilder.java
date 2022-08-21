package com.webecommerce.springboot.specification;

import com.webecommerce.springboot.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductSpecificationsBuilder {
    private final List<ProductSearchCriteria> params;

    public ProductSpecificationsBuilder() {
        params = new ArrayList<ProductSearchCriteria>();
    }

    public ProductSpecificationsBuilder with(String key, String operation, String value) {
        params.add(new ProductSearchCriteria(key, operation, value));
        return this;
    }

    public Specification<ProductEntity> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
                .map(ProductSpecification::new)
                .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = result.and(specs.get(i));
        }
        return result;
    }
}

