package com.sunnyserenade.midnightdiner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/auth")
public class AdminAuthController {

    @Autowired
    private AuthenticationManager authManager;

    @GetMapping("/status")
    public Map<String, Boolean> checkStatus() {
        // 如果能进到这里，说明已通过 Security 过滤器
        // 也可以检查 SecurityContextHolder.getContext().getAuthentication()...
        return Map.of("loggedIn", true);
    }
}
