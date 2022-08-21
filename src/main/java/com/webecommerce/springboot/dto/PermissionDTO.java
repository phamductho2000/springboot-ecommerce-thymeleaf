package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionDTO extends AbstractDTO {
    private String name;

    private String code;

    private String table;

    private String generalName;
}
