package com.webecommerce.springboot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_permission")
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "general_name")
    private String generalName;

    private String name;

    private String code;

    private String table;

    @ManyToMany(mappedBy = "permissions")
    private List<RoleEntity> roles;
}
