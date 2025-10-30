package com.cuongph.be_code.dto.response.auth;


public record AuthResponse(String token, String username, String role) {
}