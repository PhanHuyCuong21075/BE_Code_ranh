package com.cuongph.be_code.controller;

import com.cuongph.be_code.common.auth.AuthService;
import com.cuongph.be_code.dto.auth.AuthRequest;
import com.cuongph.be_code.dto.auth.RegisterRequest;
import com.cuongph.be_code.dto.response.ResponseData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseData<Object> register(@RequestBody RegisterRequest req) {
        authService.register(req);
        return new ResponseData<>().success(Map.of("message", "User registered successfully"));
    }


    @PostMapping("/login")
    public ResponseData<Object> login(@RequestBody AuthRequest req) {
        return new ResponseData<>().success(authService.login(req));
    }

}
