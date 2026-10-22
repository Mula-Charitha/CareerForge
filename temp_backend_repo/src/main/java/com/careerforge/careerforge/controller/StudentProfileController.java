package com.careerforge.careerforge.controller;

import com.careerforge.careerforge.dto.ProfileCompletionDto;
import com.careerforge.careerforge.dto.ProfileRequestDto;
import com.careerforge.careerforge.dto.ProfileResponseDto;
import com.careerforge.careerforge.service.StudentProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class StudentProfileController {

    private final StudentProfileService profileService;

    public StudentProfileController(StudentProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile() {
        String email = getAuthenticatedUserEmail();
        ProfileResponseDto profile = profileService.getProfileByEmail(email);
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<ProfileResponseDto> updateProfile(@Valid @RequestBody ProfileRequestDto request) {
        String email = getAuthenticatedUserEmail();
        ProfileResponseDto updatedProfile = profileService.updateProfileByEmail(email, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/completion")
    public ResponseEntity<ProfileCompletionDto> getProfileCompletion() {
        String email = getAuthenticatedUserEmail();
        ProfileCompletionDto completion = profileService.getProfileCompletionByEmail(email);
        return ResponseEntity.ok(completion);
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Unauthenticated user accessing profile");
        }
        return authentication.getName();
    }
}
