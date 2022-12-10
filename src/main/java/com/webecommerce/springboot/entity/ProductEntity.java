package com.webecommerce.springboot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_product")
@SQLDelete(sql = "UPDATE tbl_product SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class ProductEntity extends BaseEntity {

    @Id
    private String id;

    private String name;

    @Column(name = "short_des")
    private String shortDes;

    @Column(name = "detail_des")
    private String detailDes;

    private long price;

    @Column(name = "original_price")
    private long originalPrice;

    private int quantity;

    @Column(name = "sell_count")
    private long sellCount;

    private int status;

    private String slug;

    @Column(name = "type_product")
    private long typeProduct;

    private boolean deleted = Boolean.FALSE;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "slug_seo")
    private String slugSeo;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "tbl_product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    List<CategoryEntity> categories;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "tbl_product_attribute",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "attribute_value_id"))
    List<AttributeValueEntity> attributeValueEntities;

    @ManyToMany
    @JoinTable(
            name = "tbl_product_image",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    List<ImageEntity> images;
}
