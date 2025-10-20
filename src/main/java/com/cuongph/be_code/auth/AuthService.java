package com.cuongph.be_code.auth;

import com.cuongph.be_code.dto.AuthRequest;
import com.cuongph.be_code.dto.AuthResponse;
import com.cuongph.be_code.dto.RegisterRequest;
import com.cuongph.be_code.entity.User;
import com.cuongph.be_code.jwt.JwtUtils;
import com.cuongph.be_code.repo.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    public AuthService(UserRepository repo, PasswordEncoder encoder, AuthenticationManager authManager, JwtUtils jwtUtils) {
        this.repo = repo; this.encoder = encoder; this.authManager = authManager; this.jwtUtils = jwtUtils;
    }

    public AuthResponse login(AuthRequest req) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        UserDetails ud = (UserDetails) auth.getPrincipal();
        String token = jwtUtils.generateToken(ud);
        return new AuthResponse(token, ud.getUsername());
    }

    public void register(RegisterRequest req) {
        if (repo.existsByUsername(req.username())) {
            throw new RuntimeException("Username already taken");
        }
        User u = new User();
        u.setUsername(req.username());
        u.setPassword(encoder.encode(req.password()));
        repo.save(u);
    }
}
