package com.careerforge.careerforge.dto;

import com.careerforge.careerforge.entity.ResumeVersion;
import java.time.LocalDateTime;

public class ResumeVersionDto {

    private Long id;
    private int versionNumber;
    private String originalFileName;
    private String fileType;
    private Long fileSize;
    private LocalDateTime uploadedAt;

    public ResumeVersionDto() {
    }

    public ResumeVersionDto(ResumeVersion version) {
        if (version != null) {
            this.id = version.getId();
            this.versionNumber = version.getVersionNumber();
            this.originalFileName = version.getOriginalFileName();
            this.fileType = version.getFileType();
            this.fileSize = version.getFileSize();
            this.uploadedAt = version.getUploadedAt();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
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

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}
