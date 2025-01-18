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

    // ----------------
    // 过滤器链配置
    // ----------------
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // 关闭CSRF
                .authorizeHttpRequests(auth -> {
                    // 允许匿名访问的接口
                    auth.requestMatchers("/api/v1/reservations/**").permitAll();
                    auth.requestMatchers("/api/v1/admin/auth/login").permitAll();
                    // 需要认证的接口
                    auth.requestMatchers("/api/v1/admin/**").authenticated();
                    // 其他接口可根据需要设定
                    auth.anyRequest().permitAll();
                })
                .formLogin(form -> form.disable()); // 禁用表单登录

        return http.build();
    }

    // ----------------
    // AuthenticationManager
    // ----------------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ----------------
    // BCrypt 密码加密
    // ----------------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
