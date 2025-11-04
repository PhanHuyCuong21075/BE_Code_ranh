package com.cuongph.be_code.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JwtUtils: lớp tiện ích xử lý JWT (tạo, xác thực, trích xuất thông tin)
 * Dùng thư viện io.jsonwebtoken (jjwt)
 */
@Component
public class JwtUtils {

    // 🔐 Chuỗi bí mật dùng để ký token (khai báo trong application.properties hoặc .yml)
    @Value("${jwt.secret}")
    private String jwtSecret;

    // ⏱️ Thời gian sống của token (tính bằng milliseconds)
    @Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    /**
     * ✅ Lấy khóa ký (key) từ chuỗi secret
     * Key này phải đủ độ dài (tối thiểu 32 bytes cho HS256)
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * ✅ Sinh token mới có chứa username + roles
     *
     * @param username Tên đăng nhập
     * @param roles    Danh sách quyền (vd: ["ROLE_ADMIN", "ROLE_USER"])
     * @return Token dạng JWT đã ký
     */
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)                                  // Đặt subject là username
                .claim("roles", roles)                                 // Gắn thêm claim "roles" để chứa danh sách quyền
                .setIssuedAt(new Date())                               // Thời điểm phát hành
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // Thời điểm hết hạn
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)   // Ký bằng thuật toán HS256
                .compact();                                            // Sinh chuỗi token
    }

    /**
     * ✅ Trích xuất username (subject) từ token
     */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * ✅ Trích xuất danh sách quyền từ token
     */
    public List<String> extractAuthorities(String token) {
        Claims claims = parseClaims(token);
        Object rolesObj = claims.get("roles");

        // Nếu claim "roles" là một List thì ép kiểu và map sang String
        if (rolesObj instanceof List<?> rolesList) {
            return rolesList.stream().map(Object::toString).collect(Collectors.toList());
        }
        return List.of();
    }

    /**
     * ✅ Kiểm tra token hợp lệ
     *  - Chữ ký đúng
     *  - Username trùng khớp
     *  - Chưa hết hạn
     */
    public boolean isTokenValid(String token, String username) {
        try {
            final String extracted = extractUsername(token);
            return extracted.equals(username) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            // Token hỏng hoặc không hợp lệ
            return false;
        }
    }

    /**
     * ✅ Kiểm tra token hết hạn chưa
     */
    private boolean isTokenExpired(String token) {
        Date expiration = parseClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * ✅ Phương thức parseClaims tái sử dụng — tránh lặp code
     * @throws JwtException nếu token không hợp lệ (sai chữ ký, format, expired)
     */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
