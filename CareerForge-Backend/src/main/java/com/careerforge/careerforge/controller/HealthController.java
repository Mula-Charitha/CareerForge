package com.careerforge.careerforge.controller;

import com.careerforge.careerforge.dto.HealthResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<HealthResponseDto> getHealthStatus() {
        HealthResponseDto healthResponse = new HealthResponseDto("UP", "CareerForge Backend is running");
        return ResponseEntity.ok(healthResponse);
    }
}
