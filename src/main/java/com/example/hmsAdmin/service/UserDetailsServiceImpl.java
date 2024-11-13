package com.example.hmsAdmin.service;

import com.example.hmsAdmin.entity.Admin;
import com.example.hmsAdmin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isEmpty()) { // Check if student is present
            throw new UsernameNotFoundException("Admin not found");
        }
//        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        return new org.springframework.security.core.userdetails.User(
                admin.get().getEmail(),
                admin.get().getPassword(),
                new ArrayList<>()
        );
    }

}