package com.cuongph.be_code.common.auth;

import com.cuongph.be_code.entity.User;
import com.cuongph.be_code.entity.UserRoleEntity;
import com.cuongph.be_code.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Lấy user theo username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Lấy danh sách role từ userRoleEntity
        List<GrantedAuthority> authorities = user.getUserRoles()
                .stream()
                .map(UserRoleEntity::getRole)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode().toUpperCase()))
                .collect(Collectors.toList());

        // Trả về UserDetails của Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}