package com.webecommerce.springboot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "table_save_latest_id")
public class SavedId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public SavedId() {
    }

    public SavedId(String type, String latestId) {
        this.type = type;
        this.latestId = latestId;
    }

    private String type;

    @Column(name = "latest_id")
    private String latestId;

}
