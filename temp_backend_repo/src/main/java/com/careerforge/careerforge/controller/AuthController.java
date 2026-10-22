package com.careerforge.careerforge.controller;

import com.careerforge.careerforge.dto.AuthResponse;
import com.careerforge.careerforge.dto.LoginRequest;
import com.careerforge.careerforge.dto.RegisterRequest;
import com.careerforge.careerforge.dto.UserDto;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.UserRepository;
import com.careerforge.careerforge.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        UserDto currentUser = authService.getCurrentUser(userEmail);
        return ResponseEntity.ok(currentUser);
    }

    // TEMPORARY: Fix plain-text passwords in DB. Remove after use.
    @GetMapping("/fix-passwords")
    public ResponseEntity<Map<String, Object>> fixPasswords() {
        List<User> users = userRepository.findAll();
        Map<String, Object> result = new HashMap<>();
        int fixed = 0;
        for (User user : users) {
            String pwd = user.getPassword();
            if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$")) {
                // Plain-text password found — re-encode it
                user.setPassword(passwordEncoder.encode(pwd));
                userRepository.save(user);
                result.put(user.getEmail(), "FIXED (was plain-text)");
                fixed++;
            } else {
                result.put(user.getEmail(), "OK (already BCrypt)");
            }
        }
        result.put("_summary", fixed + " password(s) fixed");
        return ResponseEntity.ok(result);
    }
}
