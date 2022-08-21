package com.webecommerce.springboot.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "tbl_delivery_address")
public class DeliveryAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @JoinColumn(name = "province_id")
    private Long provinceId;

    @JoinColumn(name = "province_name")
    private String provinceName;

    @JoinColumn(name = "district_id")
    private Long districtId;

    @JoinColumn(name = "district_name")
    private String districtName;

    @JoinColumn(name = "wards_code")
    private String wardsCode;

    @JoinColumn(name = "wards_name")
    private String wardsName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
