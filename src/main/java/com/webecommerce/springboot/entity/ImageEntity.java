package com.webecommerce.springboot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_image")
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String url;

    private String name;

    private double size;

    @Column(name = "parent_path")
    private String parentPath;

    @ManyToMany(mappedBy = "images")
    List<ProductEntity> productEntities;

}
