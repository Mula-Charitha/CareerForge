package com.careerforge.careerforge.service;

import com.careerforge.careerforge.dto.CertificationRequestDto;
import com.careerforge.careerforge.dto.CertificationResponseDto;
import com.careerforge.careerforge.dto.CertificationStatsDto;
import com.careerforge.careerforge.entity.Certification;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.CertificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CertificationService {

    private static final Logger log = LoggerFactory.getLogger(CertificationService.class);

    private final CertificationRepository certificationRepository;
    private final FileStorageService fileStorageService;

    public CertificationService(CertificationRepository certificationRepository,
                                FileStorageService fileStorageService) {
        this.certificationRepository = certificationRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public List<CertificationResponseDto> getUserCertifications(User user) {
        return certificationRepository.findByUserOrderByIssueDateDesc(user)
                .stream()
                .map(CertificationResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CertificationResponseDto getCertificationById(User user, Long id) {
        Certification cert = certificationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Certification not found or access denied."));
        return new CertificationResponseDto(cert);
    }

    @Transactional
    public CertificationResponseDto createCertification(User user, CertificationRequestDto dto, MultipartFile file) {
        Certification cert = new Certification(user, dto.getName().trim(), dto.getIssuingOrganization().trim(), dto.getIssueDate());
        cert.setExpiryDate(dto.getExpiryDate());
        cert.setCredentialId(dto.getCredentialId());
        cert.setCredentialUrl(dto.getCredentialUrl());
        cert.setNotes(dto.getNotes());

        if (file != null && !file.isEmpty()) {
            FileStorageService.StoredFileResult stored = fileStorageService.storeFile(file);
            cert.setOriginalFileName(stored.getOriginalFileName());
            cert.setStoredFileName(stored.getStoredFileName());
            cert.setFileType(stored.getFileType());
            cert.setFileSize(stored.getFileSize());
        }

        Certification saved = certificationRepository.save(cert);
        log.info("Student '{}' added certification '{}' (ID: {})", user.getEmail(), saved.getName(), saved.getId());
        return new CertificationResponseDto(saved);
    }

    @Transactional
    public CertificationResponseDto updateCertification(User user, Long id, CertificationRequestDto dto, MultipartFile file) {
        Certification cert = certificationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Certification not found or access denied."));

        cert.setName(dto.getName().trim());
        cert.setIssuingOrganization(dto.getIssuingOrganization().trim());
        cert.setIssueDate(dto.getIssueDate());
        cert.setExpiryDate(dto.getExpiryDate());
        cert.setCredentialId(dto.getCredentialId());
        cert.setCredentialUrl(dto.getCredentialUrl());
        cert.setNotes(dto.getNotes());

        if (file != null && !file.isEmpty()) {
            // Delete old file if present
            if (cert.getStoredFileName() != null) {
                fileStorageService.deletePhysicalFile(cert.getStoredFileName());
            }
            FileStorageService.StoredFileResult stored = fileStorageService.storeFile(file);
            cert.setOriginalFileName(stored.getOriginalFileName());
            cert.setStoredFileName(stored.getStoredFileName());
            cert.setFileType(stored.getFileType());
            cert.setFileSize(stored.getFileSize());
        }

        Certification saved = certificationRepository.save(cert);
        return new CertificationResponseDto(saved);
    }

    @Transactional
    public void deleteCertification(User user, Long id) {
        Certification cert = certificationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Certification not found or access denied."));

        if (cert.getStoredFileName() != null) {
            fileStorageService.deletePhysicalFile(cert.getStoredFileName());
        }

        certificationRepository.delete(cert);
        log.info("Student '{}' deleted certification ID: {}", user.getEmail(), id);
    }

    @Transactional(readOnly = true)
    public CertificateFileResult downloadCertificateFile(User user, Long id) {
        Certification cert = certificationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Certification not found or access denied."));

        if (cert.getStoredFileName() == null || cert.getStoredFileName().isEmpty()) {
            throw new IllegalArgumentException("No file attached to this certification.");
        }

        Resource resource = fileStorageService.loadFileAsResource(cert.getStoredFileName());
        return new CertificateFileResult(resource, cert.getOriginalFileName(), cert.getFileType());
    }

    @Transactional(readOnly = true)
    public CertificationStatsDto getCertificationStats(User user) {
        long count = certificationRepository.countByUser(user);
        return new CertificationStatsDto(count);
    }

    public static class CertificateFileResult {
        private final Resource resource;
        private final String originalFileName;
        private final String fileType;

        public CertificateFileResult(Resource resource, String originalFileName, String fileType) {
            this.resource = resource;
            this.originalFileName = originalFileName;
            this.fileType = fileType;
        }

        public Resource getResource() { return resource; }
        public String getOriginalFileName() { return originalFileName; }
        public String getFileType() { return fileType; }
    }
}
