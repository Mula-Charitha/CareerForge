package com.careerforge.careerforge.dto;

import com.careerforge.careerforge.entity.Certification;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CertificationResponseDto {

    private Long id;
    private String name;
    private String issuingOrganization;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String credentialId;
    private String credentialUrl;
    private String notes;
    private String originalFileName;
    private String fileType;
    private Long fileSize;
    private boolean hasFile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CertificationResponseDto() {
    }

    public CertificationResponseDto(Certification cert) {
        if (cert != null) {
            this.id = cert.getId();
            this.name = cert.getName();
            this.issuingOrganization = cert.getIssuingOrganization();
            this.issueDate = cert.getIssueDate();
            this.expiryDate = cert.getExpiryDate();
            this.credentialId = cert.getCredentialId();
            this.credentialUrl = cert.getCredentialUrl();
            this.notes = cert.getNotes();
            this.originalFileName = cert.getOriginalFileName();
            this.fileType = cert.getFileType();
            this.fileSize = cert.getFileSize();
            this.hasFile = cert.getStoredFileName() != null && !cert.getStoredFileName().isEmpty();
            this.createdAt = cert.getCreatedAt();
            this.updatedAt = cert.getUpdatedAt();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
