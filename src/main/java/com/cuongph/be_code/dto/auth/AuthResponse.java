package com.cuongph.be_code.dto.auth;


public record AuthResponse(String token, String username, String role) {
}