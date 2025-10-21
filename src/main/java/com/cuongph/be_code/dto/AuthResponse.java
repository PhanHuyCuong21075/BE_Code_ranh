package com.cuongph.be_code.dto;


public record AuthResponse(String token, String username, String role) {
}