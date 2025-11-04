package com.cuongph.be_code.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "menus")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    @Column
    private String icon;
}
