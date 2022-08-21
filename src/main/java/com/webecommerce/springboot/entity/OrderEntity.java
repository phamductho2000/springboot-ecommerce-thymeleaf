package com.webecommerce.springboot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_order")
public class OrderEntity extends BaseEntity {
    private int status;

    private String note;

    private double total;

    @Column(name = "fee_ship")
    private double feeShip;

    @Column(name = "payment_status")
    private int paymentStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "order")
    private List<DetailOrderEntity> detailOrders;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private DeliveryAddressEntity deliveryAddress;
}

