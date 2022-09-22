package com.webecommerce.springboot.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_type_product")
@SQLDelete(sql = "UPDATE tbl_type_product SET deleted = true WHERE id=?")
public class TypeProductEntity extends BaseEntity {

    private String name;

    private boolean deleted = Boolean.FALSE;

    @OneToMany(mappedBy = "typeProductEntity")
    List<AttributeEntity> attributeEntities;

}
