package com.cuongph.be_code.common.auth;

import com.cuongph.be_code.entity.RoleEntity;
import com.cuongph.be_code.entity.UserEntity;
import com.cuongph.be_code.entity.UserRoleEntity;
import com.cuongph.be_code.repo.RolesRepository;
import com.cuongph.be_code.repo.UserRepository;
import com.cuongph.be_code.repo.UserRoleRepository;
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
    private final UserRoleRepository userRoleRepository;
    private final RolesRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1️⃣ Lấy user
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // 2️⃣ Lấy danh sách UserRoleEntity theo userId
        List<UserRoleEntity> userRoles = userRoleRepository.findByUserId(userEntity.getId());

        // 3️⃣ Lấy danh sách roleId từ userRoles
        List<Long> roleIds = userRoles.stream()
                .map(UserRoleEntity::getRoleId)
                .toList();

        // 4️⃣ Lấy RoleEntity theo roleIds
        List<RoleEntity> roles = roleRepository.findAllById(roleIds);

        // 5️⃣ Map RoleEntity -> GrantedAuthority
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getCode().toUpperCase()))
                .toList();

        // 6️⃣ Trả về UserDetails cho Spring Security
        return new org.springframework.security.core.userdetails.User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                authorities
        );
    }
}
