//package com.cuongph.be_code.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//@Entity
//@Data
//@EqualsAndHashCode(callSuper = false)
//@Table(name = "user_detail")
//public class UserDetailEntity extends BaseEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "user_id", nullable = false)
//    private Long userId;
//
//    @Column(name = "full_name")
//    private String fullName;
//
//    @Column(name = "phone")
//    private String phone;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "gender")
//    private String gender;
//
//    @Column(name = "file_path_avatar")
//    private String filePathAvatar;
//}
