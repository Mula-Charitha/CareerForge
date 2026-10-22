package com.careerforge.careerforge.controller;

import com.careerforge.careerforge.dto.MockInterviewRequestDto;
import com.careerforge.careerforge.dto.MockInterviewResponseDto;
import com.careerforge.careerforge.dto.MockInterviewStatsDto;
import com.careerforge.careerforge.entity.MockInterviewStatus;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.UserRepository;
import com.careerforge.careerforge.service.MockInterviewService;
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
@RequestMapping("/api/interviews")
public class MockInterviewController {

    private final MockInterviewService mockInterviewService;
    private final UserRepository userRepository;

    public MockInterviewController(MockInterviewService mockInterviewService, UserRepository userRepository) {
        this.mockInterviewService = mockInterviewService;
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
    public ResponseEntity<List<MockInterviewResponseDto>> getInterviews(
            @RequestParam(value = "status", required = false) MockInterviewStatus status) {
        User user = getAuthenticatedUser();
        List<MockInterviewResponseDto> list = mockInterviewService.getUserInterviews(user, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInterviewById(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            return ResponseEntity.ok(mockInterviewService.getInterviewById(user, id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createInterview(@Valid @RequestBody MockInterviewRequestDto dto) {
        try {
            User user = getAuthenticatedUser();
            MockInterviewResponseDto created = mockInterviewService.createInterview(user, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to schedule interview: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInterview(
            @PathVariable Long id,
            @Valid @RequestBody MockInterviewRequestDto dto) {
        try {
            User user = getAuthenticatedUser();
            MockInterviewResponseDto updated = mockInterviewService.updateInterview(user, id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update interview: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInterview(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            mockInterviewService.deleteInterview(user, id);
            return ResponseEntity.ok(Map.of("message", "Mock interview deleted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<MockInterviewStatsDto> getInterviewStats() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(mockInterviewService.getInterviewStats(user));
    }
}
