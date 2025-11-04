package com.cuongph.be_code.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ✅ JwtAuthFilter: Kiểm tra JWT trên mỗi request (chạy 1 lần/request).
 * Mục đích:
 *  - Đọc token từ header "Authorization"
 *  - Xác thực token hợp lệ
 *  - Gắn thông tin user (username + roles) vào SecurityContext để Spring Security nhận diện
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    // ✅ Chỉ cần JwtUtils (không dùng UserDetailsService nữa)
    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        // ✅ 1. Lấy đường dẫn hiện tại
        String path = req.getServletPath();

        // ✅ 2. Bỏ qua filter cho các endpoint public như /api/auth/**
        if (path.startsWith("/api/auth/")) {
            chain.doFilter(req, res);
            return;
        }

        // ✅ 3. Lấy Authorization header
        String header = req.getHeader("Authorization");

        // Nếu không có header hoặc không phải dạng Bearer token thì cho qua
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        // ✅ 4. Cắt chuỗi lấy token
        String token = header.substring(7);

        // ✅ 5. Trích xuất username từ token
        String username = jwtUtils.extractUsername(token);

        // ✅ 6. Kiểm tra token hợp lệ (signature đúng, chưa hết hạn)
        if (username != null && jwtUtils.isTokenValid(token, username)
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            // ✅ 7. Lấy danh sách quyền từ token
            List<String> roles = jwtUtils.extractAuthorities(token);

            // ✅ 8. Chuyển đổi roles -> GrantedAuthority để Spring hiểu
            var authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // ✅ 9. Tạo Authentication object, credentials = null (vì đã xác thực bằng JWT)
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // ✅ 10. Đính kèm chi tiết request (IP, session ID, v.v.)
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

            // ✅ 11. Đưa thông tin user đã xác thực vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // ✅ 12. Cho phép request tiếp tục đi qua filter chain
        chain.doFilter(req, res);
    }
}
