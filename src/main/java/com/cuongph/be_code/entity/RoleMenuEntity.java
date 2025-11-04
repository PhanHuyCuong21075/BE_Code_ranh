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

    // FK tới role
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    // FK tới menu
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuEntity menu;
}
