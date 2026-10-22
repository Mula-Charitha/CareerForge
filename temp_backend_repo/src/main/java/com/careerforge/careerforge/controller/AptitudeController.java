package com.careerforge.careerforge.controller;

import com.careerforge.careerforge.dto.*;
import com.careerforge.careerforge.entity.AptitudeCategory;
import com.careerforge.careerforge.entity.Difficulty;
import com.careerforge.careerforge.service.AptitudeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aptitude")
public class AptitudeController {

    private final AptitudeService aptitudeService;

    public AptitudeController(AptitudeService aptitudeService) {
        this.aptitudeService = aptitudeService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryTopicsDto>> getCategories() {
        return ResponseEntity.ok(aptitudeService.getCategories());
    }

    @GetMapping("/topics")
    public ResponseEntity<List<String>> getTopics(@RequestParam(required = false) AptitudeCategory category) {
        if (category == null) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(aptitudeService.getTopicsByCategory(category));
    }

    @GetMapping("/questions")
    public ResponseEntity<List<AptitudeQuestionDto>> getQuestions(
            @RequestParam(required = false) AptitudeCategory category,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) Difficulty difficulty
    ) {
        // Discovery API masks correct answers and explanations
        return ResponseEntity.ok(aptitudeService.getQuestions(category, topic, difficulty));
    }

    @PostMapping("/sessions")
    public ResponseEntity<AptitudeSessionResponseDto> createSession(@Valid @RequestBody AptitudeSessionRequestDto request) {
        String email = getAuthenticatedUserEmail();
        AptitudeSessionResponseDto response = aptitudeService.createSession(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/sessions/{sessionId}/questions")
    public ResponseEntity<AptitudeSessionResponseDto> getSessionQuestions(@PathVariable Long sessionId) {
        String email = getAuthenticatedUserEmail();
        return ResponseEntity.ok(aptitudeService.getSessionQuestions(email, sessionId));
    }

    @PostMapping("/sessions/{sessionId}/answers")
    public ResponseEntity<AptitudeAnswerResultDto> submitAnswer(
            @PathVariable Long sessionId,
            @Valid @RequestBody AptitudeAnswerRequestDto request
    ) {
        String email = getAuthenticatedUserEmail();
        return ResponseEntity.ok(aptitudeService.submitAnswer(email, sessionId, request));
    }

    @PostMapping("/sessions/{sessionId}/complete")
    public ResponseEntity<AptitudeSessionResultsDto> completeSession(@PathVariable Long sessionId) {
        String email = getAuthenticatedUserEmail();
        return ResponseEntity.ok(aptitudeService.completeSession(email, sessionId));
    }

    @GetMapping("/sessions/{sessionId}/results")
    public ResponseEntity<AptitudeSessionResultsDto> getSessionResults(@PathVariable Long sessionId) {
        String email = getAuthenticatedUserEmail();
        return ResponseEntity.ok(aptitudeService.getSessionResults(email, sessionId));
    }

    @GetMapping("/sessions/history")
    public ResponseEntity<List<AptitudeSessionHistoryDto>> getSessionHistory() {
        String email = getAuthenticatedUserEmail();
        return ResponseEntity.ok(aptitudeService.getSessionHistory(email));
    }

    @GetMapping("/statistics")
    public ResponseEntity<AptitudeStatisticsDto> getStatistics() {
        String email = getAuthenticatedUserEmail();
        return ResponseEntity.ok(aptitudeService.getStatistics(email));
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Unauthenticated user accessing Aptitude API");
        }
        return authentication.getName();
    }
}
