package com.webecommerce.springboot.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_attribute_value")
@SQLDelete(sql = "UPDATE tbl_attribute_value SET deleted = true WHERE attribute_value_id=?")
@Where(clause = "deleted = false")
public class AttributeValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_value_id")
    private long id;

    @Column(name = "value")
    private String value;

    private boolean deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_id")
    private AttributeEntity attributeEntity;

    @ManyToMany(mappedBy = "attributeValueEntities")
    List<ProductEntity> productEntities;
}
