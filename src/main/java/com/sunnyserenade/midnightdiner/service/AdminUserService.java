package com.sunnyserenade.midnightdiner.service;

import com.sunnyserenade.midnightdiner.entity.AdminUser;
import com.sunnyserenade.midnightdiner.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminUserService implements UserDetailsService {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUser admin = adminUserRepository.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin user not found: " + username);
        }

        // 构建 Spring Security 的 UserDetails
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + admin.getRole()));
        return new User(admin.getUsername(), admin.getPassword(), authorities);
    }

    // 由于安全性问题，创建管理员接口不暴露给前端，只能在后端手动创建管理员用户。
//    public AdminUser createAdminUser(String username, String rawPassword, String role) {
//        AdminUser user = new AdminUser();
//        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(rawPassword));
//        user.setRole(role);
//        user.setCreateTime(LocalDateTime.now());
//        user.setUpdateTime(LocalDateTime.now());
//        return adminUserRepository.save(user);
//    }
}
