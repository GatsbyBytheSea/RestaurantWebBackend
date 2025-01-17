package com.sunnyserenade.midnightdiner.config;

import com.sunnyserenade.midnightdiner.service.AdminUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    private final AdminUserService adminUserService;

    public SecurityConfig(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF
                .csrf(csrf -> csrf.disable())

                // 配置权限规则
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/reservations/**").permitAll() // 允许匿名访问
                        .requestMatchers("/api/v1/admin/auth/login").permitAll() // 允许匿名访问
                        .requestMatchers("/api/v1/admin/**").authenticated() // 需要身份验证
                        .anyRequest().permitAll() // 其他未指定的接口允许访问
                )

                // 禁用默认登录表单
                .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // 从 AuthenticationConfiguration 中获取 AuthenticationManager
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
