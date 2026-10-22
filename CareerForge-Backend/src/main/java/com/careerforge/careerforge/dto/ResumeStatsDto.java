package com.careerforge.careerforge.dto;

import java.time.LocalDateTime;

public class ResumeStatsDto {

    private long totalResumes;
    private boolean hasPrimary;
    private String primaryResumeName;
    private Long primaryResumeId;
    private LocalDateTime latestUploadDate;
    private long totalVersions;

    public ResumeStatsDto() {
    }

    public ResumeStatsDto(long totalResumes, boolean hasPrimary, String primaryResumeName,
                          Long primaryResumeId, LocalDateTime latestUploadDate, long totalVersions) {
        this.totalResumes = totalResumes;
        this.hasPrimary = hasPrimary;
        this.primaryResumeName = primaryResumeName;
        this.primaryResumeId = primaryResumeId;
        this.latestUploadDate = latestUploadDate;
        this.totalVersions = totalVersions;
    }

    public long getTotalResumes() {
        return totalResumes;
    }

    public void setTotalResumes(long totalResumes) {
        this.totalResumes = totalResumes;
    }

    public boolean isHasPrimary() {
        return hasPrimary;
    }

    public void setHasPrimary(boolean hasPrimary) {
        this.hasPrimary = hasPrimary;
    }

    public String getPrimaryResumeName() {
        return primaryResumeName;
    }

    public void setPrimaryResumeName(String primaryResumeName) {
        this.primaryResumeName = primaryResumeName;
    }

    public Long getPrimaryResumeId() {
        return primaryResumeId;
    }

    public void setPrimaryResumeId(Long primaryResumeId) {
        this.primaryResumeId = primaryResumeId;
    }

    public LocalDateTime getLatestUploadDate() {
        return latestUploadDate;
    }

    public void setLatestUploadDate(LocalDateTime latestUploadDate) {
        this.latestUploadDate = latestUploadDate;
    }

    public long getTotalVersions() {
        return totalVersions;
    }

    public void setTotalVersions(long totalVersions) {
        this.totalVersions = totalVersions;
    }
}
