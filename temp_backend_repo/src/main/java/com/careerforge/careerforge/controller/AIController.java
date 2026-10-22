package com.careerforge.careerforge.controller;

import com.careerforge.careerforge.dto.AIChatRequestDto;
import com.careerforge.careerforge.dto.AIChatResponseDto;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.UserRepository;
import com.careerforge.careerforge.service.AIService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;
    private final UserRepository userRepository;

    public AIController(AIService aiService, UserRepository userRepository) {
        this.aiService = aiService;
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

    @PostMapping("/chat")
    public ResponseEntity<AIChatResponseDto> chat(@Valid @RequestBody AIChatRequestDto request) {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(aiService.processChat(user, request));
    }

    @GetMapping("/history")
    public ResponseEntity<List<AIChatResponseDto>> getHistory() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(aiService.getChatHistory(user));
    }

    @DeleteMapping("/history")
    public ResponseEntity<Map<String, String>> clearHistory() {
        User user = getAuthenticatedUser();
        aiService.clearHistory(user);
        return ResponseEntity.ok(Map.of("message", "Chat history cleared successfully"));
    }

    @GetMapping("/prompts")
    public ResponseEntity<List<Map<String, String>>> getSuggestedPrompts() {
        List<Map<String, String>> prompts = Arrays.asList(
                Map.of("title", "Tell Me About Yourself", "prompt", "How should I structure my answer to 'Tell me about yourself' for an SDE fresher role?", "category", "BEHAVIORAL"),
                Map.of("title", "STAR Method Guide", "prompt", "Explain the STAR method for behavioral interviews with an example.", "category", "BEHAVIORAL"),
                Map.of("title", "ATS Resume Optimization", "prompt", "How can I optimize my software engineer resume bullet points for ATS scanners?", "category", "RESUME"),
                Map.of("title", "Top Dynamic Programming Patterns", "prompt", "What are the most frequent Dynamic Programming patterns asked in tech interviews?", "category", "DSA"),
                Map.of("title", "Amazon Leadership Principles", "prompt", "How to prepare for Amazon Leadership Principles rounds?", "category", "COMPANY"),
                Map.of("title", "Questions for the Interviewer", "prompt", "What are the best questions to ask the interviewer at the end of a round?", "category", "HR")
        );
        return ResponseEntity.ok(prompts);
    }
}
