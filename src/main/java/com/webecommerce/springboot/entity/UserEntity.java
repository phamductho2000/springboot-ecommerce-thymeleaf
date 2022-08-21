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
@Table(name = "tbl_user")
@SQLDelete(sql = "UPDATE tbl_user SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class UserEntity extends BaseEntity {

    private String name;

    private String email;

    private String address;

    private int phone;

    private String username;

    private String avatar;

    private String password;

    private boolean status = Boolean.TRUE;

    private boolean deleted = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authProvider;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "tbl_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roles;

    @OneToMany(mappedBy = "user")
    private List<OrderEntity> orders;

    @OneToMany(mappedBy = "userEntity")
    private List<DeliveryAddressEntity> deliveryAddresses;
}

