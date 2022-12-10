package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class AbstractDTO {

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private String createdBy;

    private String modifiedBy;
}
