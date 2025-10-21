package com.cuongph.be_code.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "role_menu")
public class RoleMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Role name, ví dụ "ADMIN", "USER"
    @Column(nullable = false)
    private String roleName;

    @ManyToMany
    @JoinTable(
            name = "role_menu_mapping",
            joinColumns = @JoinColumn(name = "role_menu_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private Set<Menu> menus;
}
