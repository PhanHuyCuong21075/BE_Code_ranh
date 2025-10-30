package com.cuongph.be_code.common.auth;

import com.cuongph.be_code.dto.response.auth.AuthRequest;
import com.cuongph.be_code.dto.response.auth.AuthResponse;
import com.cuongph.be_code.dto.response.auth.RegisterRequest;
import com.cuongph.be_code.entity.User;
import com.cuongph.be_code.exception.BusinessException;
import com.cuongph.be_code.jwt.JwtUtils;
import com.cuongph.be_code.repo.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public AuthService(UserRepository repo, PasswordEncoder encoder, AuthenticationManager authManager, JwtUtils jwtUtils, UserRepository userRepository) {
        this.repo = repo;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    public AuthResponse login(AuthRequest req) {
        if (!repo.existsByUsername(req.username())) {
            throw new BusinessException("User not found", "USER_NOT_FOUND", 404);
        }

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password())
            );
            UserDetails ud = (UserDetails) auth.getPrincipal();
            String token = jwtUtils.generateToken(ud);

            User u = repo.findByUsername(req.username())
                    .orElseThrow(() -> new UsernameNotFoundException("Not found"));

            return new AuthResponse(token, ud.getUsername(), u.getRole());

        } catch (BadCredentialsException ex) {
            throw new BusinessException("Invalid credentials", "INVALID_CREDENTIALS", 401);
        }
    }


    public void register(RegisterRequest req) {
        if (repo.existsByUsername(req.username())) {
            throw new BusinessException("Username already taken", "USERNAME_TAKEN", 400);
        }

        if (repo.existsByEmail(req.email())) {
            throw new BusinessException("Email already taken", "EMAIL_TAKEN", 400);
        }

        if (!req.password().equals(req.confirmPassword())) {
            throw new BusinessException("Passwords do not match", "PASSWORD_MISMATCH", 400);
        }

        User u = new User();
        u.setUsername(req.username());
        u.setPassword(encoder.encode(req.password()));
        u.setEmail(req.email());  // 👈 bạn để nhầm `req.username()` trong email
        u.setRole("USER"); // nếu có role mặc định
        repo.save(u);
    }

}
