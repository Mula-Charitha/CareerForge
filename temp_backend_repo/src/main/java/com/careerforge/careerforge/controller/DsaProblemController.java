package com.careerforge.careerforge.controller;

import com.careerforge.careerforge.dto.DsaProblemRequestDto;
import com.careerforge.careerforge.dto.DsaProblemResponseDto;
import com.careerforge.careerforge.dto.DsaStatisticsDto;
import com.careerforge.careerforge.entity.Difficulty;
import com.careerforge.careerforge.entity.Platform;
import com.careerforge.careerforge.entity.Status;
import com.careerforge.careerforge.service.DsaProblemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dsa")
public class DsaProblemController {

    private final DsaProblemService dsaProblemService;

    public DsaProblemController(DsaProblemService dsaProblemService) {
        this.dsaProblemService = dsaProblemService;
    }

    @GetMapping("/problems")
    public ResponseEntity<List<DsaProblemResponseDto>> getProblems(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) Difficulty difficulty,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Platform platform,
            @RequestParam(required = false) String search
    ) {
        String email = getAuthenticatedUserEmail();
        List<DsaProblemResponseDto> problems = dsaProblemService.getProblems(email, topic, difficulty, status, platform, search);
        return ResponseEntity.ok(problems);
    }

    @GetMapping("/problems/{id}")
    public ResponseEntity<DsaProblemResponseDto> getProblemById(@PathVariable Long id) {
        String email = getAuthenticatedUserEmail();
        DsaProblemResponseDto problem = dsaProblemService.getProblemById(email, id);
        return ResponseEntity.ok(problem);
    }

    @PostMapping("/problems")
    public ResponseEntity<DsaProblemResponseDto> createProblem(@Valid @RequestBody DsaProblemRequestDto request) {
        String email = getAuthenticatedUserEmail();
        DsaProblemResponseDto created = dsaProblemService.createProblem(email, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/problems/{id}")
    public ResponseEntity<DsaProblemResponseDto> updateProblem(
            @PathVariable Long id,
            @Valid @RequestBody DsaProblemRequestDto request
    ) {
        String email = getAuthenticatedUserEmail();
        DsaProblemResponseDto updated = dsaProblemService.updateProblem(email, id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/problems/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {
        String email = getAuthenticatedUserEmail();
        dsaProblemService.deleteProblem(email, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/statistics")
    public ResponseEntity<DsaStatisticsDto> getStatistics() {
        String email = getAuthenticatedUserEmail();
        DsaStatisticsDto stats = dsaProblemService.getStatistics(email);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/topics")
    public ResponseEntity<List<String>> getTopics() {
        return ResponseEntity.ok(dsaProblemService.getTopics());
    }

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Unauthenticated user accessing DSA API");
        }
        return authentication.getName();
    }
}
