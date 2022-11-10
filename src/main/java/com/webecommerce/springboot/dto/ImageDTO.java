package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageDTO {

    private Long id;

    private String url;

    private String name;

    private Double size;

    public ImageDTO() {}

    public ImageDTO(String url, String name, Double size) {
        this.url = url;
        this.name = name;
        this.size = size;
    }

    public ImageDTO(Long id, String url, String name, Double size) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.size = size;
    }
}
