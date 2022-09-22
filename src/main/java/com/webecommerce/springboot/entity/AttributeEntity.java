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
@Table(name = "tbl_attribute")
@SQLDelete(sql = "UPDATE tbl_attribute SET deleted = true WHERE attribute_id=?")
@Where(clause = "deleted = false")
public class AttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attribute_id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    private boolean deleted = Boolean.FALSE;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "attributeEntity")
    private List<AttributeValueEntity> attributeValueEntities;

    @ManyToOne
    @JoinColumn(name = "type_product_id")
    private TypeProductEntity typeProductEntity;
}
