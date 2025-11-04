package com.cuongph.be_code.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppProperties {

    @Value("${jwt.access.validity}")
    public Long jwtAccessValidity;

    @Value("${jwt.refresh.validity}")
    public Long jwtRefreshValidity;

    @Value("${jwt.check.validity}")
    public Long jwtCheckValidity;

    @Value("${cors.allowed.origins}")
    public String corsAllowedOrigins;

    @Value("${jwt.secret}")
    public String jwtSecret;

    // ✅ Chế độ dev (bỏ qua mã hoá mật khẩu)
    @Value("${app.dev-mode:false}")
    public boolean devMode;
}
