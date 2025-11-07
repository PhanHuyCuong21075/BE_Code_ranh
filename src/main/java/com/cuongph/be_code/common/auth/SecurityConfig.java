package com.cuongph.be_code.common.auth;

import com.cuongph.be_code.common.config.provider.CustomAuthenticationProvider;
import com.cuongph.be_code.jwt.JwtAuthFilter;
import com.cuongph.be_code.jwt.JwtUtils;
import com.cuongph.be_code.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationProvider customAuthProvider;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Value("${cors.allowed.origins}")
    private String corsOrigins;

    public SecurityConfig(CustomAuthenticationProvider customAuthProvider, JwtUtils jwtUtils, UserRepository userRepository) {
        this.customAuthProvider = customAuthProvider;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(customAuthProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtUtils,userRepository);

        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration cfg = new CorsConfiguration();
                    cfg.setAllowedOriginPatterns(List.of("*"));
                    cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    cfg.setAllowedHeaders(List.of("*"));
                    cfg.setAllowCredentials(true);
                    return cfg;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/role/list").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
