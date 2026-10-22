package com.careerforge.careerforge.dto;

import com.careerforge.careerforge.entity.Resume;
import com.careerforge.careerforge.entity.ResumeVersion;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResumeResponseDto {

    private Long id;
    private String resumeName;
    private boolean isPrimary;
    private int currentVersion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ResumeVersionDto latestVersion;
    private int versionsCount;
    private List<ResumeVersionDto> versionHistory = new ArrayList<>();

    public ResumeResponseDto() {
    }

    public ResumeResponseDto(Resume resume, ResumeVersion latest, List<ResumeVersion> allVersions) {
        if (resume != null) {
            this.id = resume.getId();
            this.resumeName = resume.getResumeName();
            this.isPrimary = resume.isPrimary();
            this.currentVersion = resume.getCurrentVersion();
            this.createdAt = resume.getCreatedAt();
            this.updatedAt = resume.getUpdatedAt();

            if (latest != null) {
                this.latestVersion = new ResumeVersionDto(latest);
            }
            if (allVersions != null) {
                this.versionsCount = allVersions.size();
                for (ResumeVersion v : allVersions) {
                    this.versionHistory.add(new ResumeVersionDto(v));
                }
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
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

    public ResumeVersionDto getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(ResumeVersionDto latestVersion) {
        this.latestVersion = latestVersion;
    }

    public int getVersionsCount() {
        return versionsCount;
    }

    public void setVersionsCount(int versionsCount) {
        this.versionsCount = versionsCount;
    }

    public List<ResumeVersionDto> getVersionHistory() {
        return versionHistory;
    }

    public void setVersionHistory(List<ResumeVersionDto> versionHistory) {
        this.versionHistory = versionHistory;
    }
}
