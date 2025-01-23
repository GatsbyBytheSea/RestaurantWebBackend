package com.sunnyserenade.midnightdiner.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;

@Configuration
public class SecurityConfig {

    // 过滤器链配置
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
                    // 对其他接口放行
                    auth.anyRequest().permitAll();
                })
                .formLogin(form -> form
                        .loginProcessingUrl("/api/v1/admin/auth/login")
                        .successHandler(authenticationSuccessHandler())  // 自定义成功处理器
                        .failureHandler(authenticationFailureHandler())  // 自定义失败处理器
                        .permitAll()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );
        return http.build();
    }

    // AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // BCrypt 密码加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 自定义登录成功处理器
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"message\":\"Login success\"}");
            writer.flush();
        };
    }

    // 自定义登录失败处理器
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write("{\"error\":\"Invalid credentials\"}");
            writer.flush();
        };
    }
}
