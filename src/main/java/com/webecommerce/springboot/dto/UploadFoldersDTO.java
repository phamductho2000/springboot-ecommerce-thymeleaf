package com.webecommerce.springboot.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class UploadFoldersDTO extends AbstractDTO {

    private String name;

    private String parentPath;

    private List<UploadFoldersDTO> children;
}
