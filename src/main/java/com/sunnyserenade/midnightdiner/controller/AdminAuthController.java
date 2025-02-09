package com.sunnyserenade.midnightdiner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller that handles authentication-related operations for Admin users.
 */
@RestController
@RequestMapping("/api/v1/admin/auth")
public class AdminAuthController {

    /**
     * The AuthenticationManager is responsible for processing authentication requests.
     */
    @Autowired
    private AuthenticationManager authManager;

    /**
     * Returns a status indicating if the admin is logged in.
     *
     * @return a Map with a "loggedIn" key set to true if the user is authenticated.
     */
    @GetMapping("/status")
    public Map<String, Boolean> checkStatus() {
        // For simplicity, always returns true in this example.
        // In a real-world scenario, you'd derive this from the current security context.
        return Map.of("loggedIn", true);
    }
}
