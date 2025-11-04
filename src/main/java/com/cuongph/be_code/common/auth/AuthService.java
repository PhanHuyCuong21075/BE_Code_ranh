package com.cuongph.be_code.common.auth;

import com.cuongph.be_code.dto.auth.AuthRequest;
import com.cuongph.be_code.dto.auth.AuthResponse;
import com.cuongph.be_code.dto.auth.RegisterRequest;
import com.cuongph.be_code.entity.RoleEntity;
import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.entity.UserRoleEntity;
import com.cuongph.be_code.exception.BusinessException;
import com.cuongph.be_code.jwt.JwtUtils;
import com.cuongph.be_code.repo.RolesRepository;
import com.cuongph.be_code.repo.UserRepository;
import com.cuongph.be_code.repo.UserRoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RolesRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;

    public AuthService(
            UserRepository userRepository,
            RolesRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder encoder,
            AuthenticationManager authManager,
            JwtUtils jwtUtils
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
    }

    // ✅ Login: xác thực qua AuthenticationManager + CustomAuthenticationProvider
    public AuthResponse login(AuthRequest req) {
        if (!userRepository.existsByUsername(req.username())) {
            throw new BusinessException("User not found", "USER_NOT_FOUND", 404);
        }

        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username(), req.password())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();

            // ✅ Convert authorities (Collection<GrantedAuthority>) → List<String>
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(a -> a.getAuthority()) // Lấy tên quyền
                    .collect(Collectors.toList());

            // ✅ Sinh token chuẩn
            String token = jwtUtils.generateToken(userDetails.getUsername(), roles);

            // ✅ Lấy entity để trả thêm info
            UserEntity userEntity = userRepository.findByUsername(req.username())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Ghép lại response
            String rolesStr = String.join(",", roles);
            return new AuthResponse(token, userEntity.getUsername(), rolesStr);

        } catch (BadCredentialsException ex) {
            throw new BusinessException("Invalid credentials", "INVALID_CREDENTIALS", 401);
        }
    }


    // ✅ Đăng ký: tạo user + gán role mặc định
    @Transactional
    public void register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            throw new BusinessException("Username already taken", "USERNAME_TAKEN", 400);
        }

        if (userRepository.existsByEmail(req.email())) {
            throw new BusinessException("Email already taken", "EMAIL_TAKEN", 400);
        }

        if (!req.password().equals(req.confirmPassword())) {
            throw new BusinessException("Passwords do not match", "PASSWORD_MISMATCH", 400);
        }

        // Tạo user mới
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(req.username());
        userEntity.setPassword(encoder.encode(req.password()));
        userEntity.setEmail(req.email());
        userRepository.save(userEntity);

        // Gán role mặc định "USER"
        RoleEntity defaultRole = roleRepository.findByCode("USER")
                .orElseThrow(() -> new BusinessException("Default role not found", "ROLE_NOT_FOUND", 500));

        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setUserId(userEntity.getId());
        userRole.setRoleId(defaultRole.getId());
        userRoleRepository.save(userRole);
    }
}
