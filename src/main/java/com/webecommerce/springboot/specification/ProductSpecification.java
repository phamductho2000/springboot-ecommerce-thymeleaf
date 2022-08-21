package com.webecommerce.springboot.specification;

import com.webecommerce.springboot.entity.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification implements Specification<ProductEntity> {

    private ProductSearchCriteria criteria;

    public ProductSpecification(ProductSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ProductEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperator().equalsIgnoreCase("=")) {
            List<Predicate> list = new ArrayList<>();
            if (criteria.getValue().startsWith("p")) {
                String value = criteria.getValue().substring(2);
                if(value.contains("to")) {
                    long min = Long.parseLong(value.split("to")[0]);
                    long max = Long.parseLong(value.split("to")[1]);
                    list.add(builder.between(root.get(criteria.getField()), min, max));

                }
                else if(value.equalsIgnoreCase("asc")) {
                    list.add((Predicate) builder.asc(root.get(criteria.getField())));
                }
                else if(value.equalsIgnoreCase("desc")) {
                    list.add((Predicate) builder.desc(root.get(criteria.getField())));
                }
                else {
                    list.add(builder.like(root.get(criteria.getField()), "%" + value + "%"));
                }
            }
            if(criteria.getValue().startsWith("a"))
            {
                String value = criteria.getValue().substring(2);
                Join<ProductEntity, AttributeValueEntity> joinAttrValue = root.join("attributeValueEntities");
                Join<AttributeValueEntity, AttributeEntity> joinAttr = joinAttrValue.join("attributeEntity");
                list.add(builder.equal(joinAttr.get(AttributeEntity_.NAME), criteria.getField()));
                list.add(builder.like(joinAttrValue.get(AttributeValueEntity_.VALUE), "%" + value + "%"));
            }
            Predicate[] p = new Predicate[list.size()];
            return builder.and(list.toArray(p));
        }
        return null;
    }
}
