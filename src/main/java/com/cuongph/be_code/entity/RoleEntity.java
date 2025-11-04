package com.cuongph.be_code.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "roles")
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = -3508486844010787792L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;
}
