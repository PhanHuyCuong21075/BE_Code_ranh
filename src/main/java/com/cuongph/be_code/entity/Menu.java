package com.cuongph.be_code.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 200)
    private String path;

    @Column(length = 100)
    private String code;
}
