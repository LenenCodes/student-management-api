package com.len.student_app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.len.student_app.dto.RegisterRequestDTO;
import com.len.student_app.entity.AppUser;
import com.len.student_app.repository.AppUserRepository;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequestDTO requestDTO) {

        if (appUserRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        AppUser user = new AppUser();

        user.setEmail(requestDTO.getEmail());

        user.setPassword(
                passwordEncoder.encode(requestDTO.getPassword())
        );

        user.setRole("USER");

        appUserRepository.save(user);
    }
}