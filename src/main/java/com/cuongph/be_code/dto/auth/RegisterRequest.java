package com.cuongph.be_code.dto.auth;

public record RegisterRequest(String username, String password, String email,
                              String confirmPassword, String roleCode,String gender,String filePathAvatar,
                              String phone,String fullName) {}