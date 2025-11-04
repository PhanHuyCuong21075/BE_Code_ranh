package com.cuongph.be_code.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "role_menu")
public class RoleMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;
}
