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
@Table(name = "tbl_category")
@SQLDelete(sql = "UPDATE tbl_category SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class CategoryEntity extends BaseEntity  {

    private String name;

    private boolean status = Boolean.TRUE;

    @Column(name = "parent_id")
    private Long parentId;

    private String slug;

    private boolean deleted = Boolean.FALSE;

    @ManyToMany(mappedBy = "categories")
    List<ProductEntity> productEntities;
}
