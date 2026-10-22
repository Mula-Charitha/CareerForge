package com.careerforge.careerforge.controller;

import com.careerforge.careerforge.dto.JobApplicationRequestDto;
import com.careerforge.careerforge.dto.JobApplicationResponseDto;
import com.careerforge.careerforge.dto.JobApplicationStatsDto;
import com.careerforge.careerforge.entity.JobApplicationStatus;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.UserRepository;
import com.careerforge.careerforge.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;
    private final UserRepository userRepository;

    public JobApplicationController(JobApplicationService jobApplicationService, UserRepository userRepository) {
        this.jobApplicationService = jobApplicationService;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponseDto>> getApplications(
            @RequestParam(value = "status", required = false) JobApplicationStatus status,
            @RequestParam(value = "search", required = false) String search) {
        User user = getAuthenticatedUser();
        List<JobApplicationResponseDto> list = jobApplicationService.getUserApplications(user, status, search);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            return ResponseEntity.ok(jobApplicationService.getApplicationById(user, id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createApplication(@Valid @RequestBody JobApplicationRequestDto dto) {
        try {
            User user = getAuthenticatedUser();
            JobApplicationResponseDto created = jobApplicationService.createApplication(user, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to create job application: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequestDto dto) {
        try {
            User user = getAuthenticatedUser();
            JobApplicationResponseDto updated = jobApplicationService.updateApplication(user, id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update job application: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApplication(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            jobApplicationService.deleteApplication(user, id);
            return ResponseEntity.ok(Map.of("message", "Job application deleted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<JobApplicationStatsDto> getApplicationStats() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(jobApplicationService.getApplicationStats(user));
    }
}
