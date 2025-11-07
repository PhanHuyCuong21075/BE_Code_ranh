package com.cuongph.be_code.jwt;

import com.cuongph.be_code.dto.userCurrent.UserInfoModel;
import com.cuongph.be_code.repo.UserRepository;
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
 * - Đọc token từ header "Authorization"
 * - Xác thực token hợp lệ
 * - Gắn thông tin user (username + roles) vào SecurityContext để Spring Security nhận diện
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    // ✅ Chỉ cần JwtUtils (không dùng UserDetailsService nữa)
    public JwtAuthFilter(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        // ✅ 1. Lấy đường dẫn hiện tại
        String path = req.getServletPath();

        // ✅ 2. Bỏ qua filter cho các endpoint public như /api/auth/**
        List<String> publicPaths = List.of("/api/auth/", "/api/role/list/");
        for (String p : publicPaths) {
            if (path.startsWith(p)) {
                chain.doFilter(req, res);
                return;
            }
        }

        // ✅ 3. Lấy Authorization header
        String header = req.getHeader("Authorization");
        String token = header.substring(7);
        // Nếu không có header hoặc không phải dạng Bearer token thì cho qua
        if (!header.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        String username = null;
        try {
            username = jwtUtils.extractUsername(token);
        } catch (Exception e) {
            // (Không làm gì, để filter tiếp tục và trả về 401/403 sau)
            logger.warn("JWT token không hợp lệ: " + e.getMessage());
        }


        // ✅ 5. Kiểm tra token hợp lệ VÀ CHƯA CÓ XÁC THỰC
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserInfoModel userInfo = this.userRepository.findByUsername(username)
                    .map(userEntity -> {
                        // Bắt đầu ánh xạ đơn giản
                        UserInfoModel model = new UserInfoModel();

                        // Map các trường khớp tên hoặc logic
                        model.setId(userEntity.getId());
                        model.setUserName(userEntity.getUsername());


                        return model;
                    })
                    .orElse(null);

            // Nếu tìm thấy user VÀ token hợp lệ
            if (userInfo != null && jwtUtils.isTokenValid(token, userInfo.getUserName())) {

                // (Phần 7, 8 - Lấy quyền - GIỮ NGUYÊN)
                List<String> roles = jwtUtils.extractAuthorities(token);
                var authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // ----- 9. 🔥 THAY ĐỔI QUAN TRỌNG NHẤT -----
                // Đặt toàn bộ đối tượng 'userInfo' làm principal
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userInfo, // <-- KHÔNG PHẢI 'username' nữa
                                null,
                                authorities
                        );

                // (Phần 10, 11 - GIỮ NGUYÊN)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // ✅ 12. Cho phép request tiếp tục
        chain.doFilter(req, res);
    }
}
