package com.careerforge.careerforge.controller;

import com.careerforge.careerforge.dto.ResumeRenameRequestDto;
import com.careerforge.careerforge.dto.ResumeResponseDto;
import com.careerforge.careerforge.dto.ResumeStatsDto;
import com.careerforge.careerforge.dto.ResumeVersionDto;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.UserRepository;
import com.careerforge.careerforge.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserRepository userRepository;

    public ResumeController(ResumeService resumeService, UserRepository userRepository) {
        this.resumeService = resumeService;
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

    /**
     * Upload a new resume (creates Resume + Version 1).
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "resumeName", required = false) String resumeName) {
        try {
            User user = getAuthenticatedUser();
            ResumeResponseDto response = resumeService.uploadResume(user, resumeName, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error uploading resume: " + e.getMessage()));
        }
    }

    /**
     * Get all resumes of authenticated student.
     */
    @GetMapping
    public ResponseEntity<List<ResumeResponseDto>> getUserResumes() {
        User user = getAuthenticatedUser();
        List<ResumeResponseDto> list = resumeService.getUserResumes(user);
        return ResponseEntity.ok(list);
    }

    /**
     * Get resume metadata and version history by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getResumeById(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            ResumeResponseDto response = resumeService.getResumeById(user, id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Download authorized resume file (latest by default, or specific version).
     */
    @GetMapping("/{id}/download")
    public ResponseEntity<?> downloadResume(
            @PathVariable Long id,
            @RequestParam(value = "version", required = false) Integer version) {
        try {
            User user = getAuthenticatedUser();
            ResumeService.DownloadResult result = resumeService.downloadResumeFile(user, id, version);

            MediaType mediaType;
            if ("PDF".equalsIgnoreCase(result.getFileType())) {
                mediaType = MediaType.APPLICATION_PDF;
            } else {
                mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            }

            String encodedFilename = URLEncoder.encode(result.getOriginalFileName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getOriginalFileName() + "\"; filename*=UTF-8''" + encodedFilename)
                    .body(result.getResource());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to download resume file: " + e.getMessage()));
        }
    }

    /**
     * Rename resume display name.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> renameResume(
            @PathVariable Long id,
            @Valid @RequestBody ResumeRenameRequestDto dto) {
        try {
            User user = getAuthenticatedUser();
            ResumeResponseDto updated = resumeService.renameResume(user, id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Upload a new version to an existing resume.
     */
    @PostMapping(value = "/{id}/versions", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadNewVersion(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            User user = getAuthenticatedUser();
            ResumeResponseDto updated = resumeService.uploadNewVersion(user, id, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to upload new version: " + e.getMessage()));
        }
    }

    /**
     * Get version history of an authorized resume.
     */
    @GetMapping("/{id}/versions")
    public ResponseEntity<?> getResumeVersions(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            List<ResumeVersionDto> versions = resumeService.getResumeVersions(user, id);
            return ResponseEntity.ok(versions);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Mark resume as primary (unsets any previous primary).
     */
    @PutMapping("/{id}/primary")
    public ResponseEntity<?> setPrimaryResume(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            ResumeResponseDto updated = resumeService.setPrimaryResume(user, id);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Delete resume and all physical files.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResume(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            resumeService.deleteResume(user, id);
            return ResponseEntity.ok(Map.of("message", "Resume deleted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to delete resume: " + e.getMessage()));
        }
    }

    /**
     * Get resume statistics for dashboard and overview.
     */
    @GetMapping("/stats")
    public ResponseEntity<ResumeStatsDto> getResumeStats() {
        User user = getAuthenticatedUser();
        ResumeStatsDto stats = resumeService.getResumeStats(user);
        return ResponseEntity.ok(stats);
    }
}
