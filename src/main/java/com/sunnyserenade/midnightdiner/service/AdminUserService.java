package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.entity.AdminUser;
import com.sunnyserenade.midnightdiner.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for handling admin user details and authentication.
 */
@Service
public class AdminUserService implements UserDetailsService {

    /**
     * Repository for accessing admin user data.
     */
    @Autowired
    private AdminUserRepository adminUserRepository;

    /**
     * Component for encoding and verifying passwords.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Loads user-specific data for Spring Security authentication.
     *
     * @param username the username identifying the user whose data is required
     * @return the fully populated user details (username, password, authorities)
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin = adminUserRepository.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin user not found: " + username);
        }

        // Construct authorities based on the admin user's role.
        List<SimpleGrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + admin.getRole()));

        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }
}
