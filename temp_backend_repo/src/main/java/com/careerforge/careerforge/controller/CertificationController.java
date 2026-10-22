package com.careerforge.careerforge.controller;

import com.careerforge.careerforge.dto.CertificationRequestDto;
import com.careerforge.careerforge.dto.CertificationResponseDto;
import com.careerforge.careerforge.dto.CertificationStatsDto;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.UserRepository;
import com.careerforge.careerforge.service.CertificationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certifications")
public class CertificationController {

    private final CertificationService certificationService;
    private final UserRepository userRepository;

    public CertificationController(CertificationService certificationService, UserRepository userRepository) {
        this.certificationService = certificationService;
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
    public ResponseEntity<List<CertificationResponseDto>> getCertifications() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(certificationService.getUserCertifications(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCertificationById(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            return ResponseEntity.ok(certificationService.getCertificationById(user, id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createCertificationMultipart(
            @RequestParam("name") String name,
            @RequestParam("issuingOrganization") String issuingOrganization,
            @RequestParam("issueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
            @RequestParam(value = "expiryDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,
            @RequestParam(value = "credentialId", required = false) String credentialId,
            @RequestParam(value = "credentialUrl", required = false) String credentialUrl,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            User user = getAuthenticatedUser();

            CertificationRequestDto dto = new CertificationRequestDto();
            dto.setName(name);
            dto.setIssuingOrganization(issuingOrganization);
            dto.setIssueDate(issueDate);
            dto.setExpiryDate(expiryDate);
            dto.setCredentialId(credentialId);
            dto.setCredentialUrl(credentialUrl);
            dto.setNotes(notes);

            CertificationResponseDto response = certificationService.createCertification(user, dto, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to create certification: " + e.getMessage()));
        }
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createCertificationJson(@Valid @RequestBody CertificationRequestDto dto) {
        try {
            User user = getAuthenticatedUser();
            CertificationResponseDto response = certificationService.createCertification(user, dto, null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateCertificationMultipart(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("issuingOrganization") String issuingOrganization,
            @RequestParam("issueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate,
            @RequestParam(value = "expiryDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expiryDate,
            @RequestParam(value = "credentialId", required = false) String credentialId,
            @RequestParam(value = "credentialUrl", required = false) String credentialUrl,
            @RequestParam(value = "notes", required = false) String notes,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            User user = getAuthenticatedUser();

            CertificationRequestDto dto = new CertificationRequestDto();
            dto.setName(name);
            dto.setIssuingOrganization(issuingOrganization);
            dto.setIssueDate(issueDate);
            dto.setExpiryDate(expiryDate);
            dto.setCredentialId(credentialId);
            dto.setCredentialUrl(credentialUrl);
            dto.setNotes(notes);

            CertificationResponseDto response = certificationService.updateCertification(user, id, dto, file);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to update certification: " + e.getMessage()));
        }
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> updateCertificationJson(
            @PathVariable Long id,
            @Valid @RequestBody CertificationRequestDto dto) {
        try {
            User user = getAuthenticatedUser();
            CertificationResponseDto response = certificationService.updateCertification(user, id, dto, null);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertification(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            certificationService.deleteCertification(user, id);
            return ResponseEntity.ok(Map.of("message", "Certification deleted successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<?> downloadCertificateFile(@PathVariable Long id) {
        try {
            User user = getAuthenticatedUser();
            CertificationService.CertificateFileResult fileResult = certificationService.downloadCertificateFile(user, id);

            MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
            if ("PDF".equalsIgnoreCase(fileResult.getFileType())) {
                mediaType = MediaType.APPLICATION_PDF;
            } else if ("DOCX".equalsIgnoreCase(fileResult.getFileType())) {
                mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            }

            String encodedFilename = URLEncoder.encode(fileResult.getOriginalFileName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResult.getOriginalFileName() + "\"; filename*=UTF-8''" + encodedFilename)
                    .body(fileResult.getResource());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<CertificationStatsDto> getCertificationStats() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(certificationService.getCertificationStats(user));
    }
}
