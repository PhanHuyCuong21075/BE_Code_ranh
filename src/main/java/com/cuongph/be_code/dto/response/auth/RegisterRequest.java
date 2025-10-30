package com.cuongph.be_code.dto.response.auth;

public record RegisterRequest(String username, String password, String email, String confirmPassword) {}