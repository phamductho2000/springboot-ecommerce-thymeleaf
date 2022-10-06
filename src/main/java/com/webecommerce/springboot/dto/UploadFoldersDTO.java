package com.webecommerce.springboot.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UploadFoldersDTO {

    private String name;

    private String parentPath;

    private String path;

    private List<UploadFoldersDTO> children;
}
