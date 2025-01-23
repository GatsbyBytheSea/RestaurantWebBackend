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
        // Check if user is logged in
        return Map.of("loggedIn", true);
    }
}
