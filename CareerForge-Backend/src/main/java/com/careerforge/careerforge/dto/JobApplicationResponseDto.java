package com.careerforge.careerforge.dto;

import com.careerforge.careerforge.entity.JobApplication;
import com.careerforge.careerforge.entity.JobApplicationStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JobApplicationResponseDto {

    private Long id;
    private String companyName;
    private String jobRole;
    private String jobLink;
    private String location;
    private String jobType;
    private LocalDate applicationDate;
    private LocalDate deadline;
    private JobApplicationStatus status;
    private LocalDate interviewDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JobApplicationResponseDto() {
    }

    public JobApplicationResponseDto(JobApplication entity) {
        this.id = entity.getId();
        this.companyName = entity.getCompanyName();
        this.jobRole = entity.getJobRole();
        this.jobLink = entity.getJobLink();
        this.location = entity.getLocation();
        this.jobType = entity.getJobType();
        this.applicationDate = entity.getApplicationDate();
        this.deadline = entity.getDeadline();
        this.status = entity.getStatus();
        this.interviewDate = entity.getInterviewDate();
        this.notes = entity.getNotes();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getJobLink() {
        return jobLink;
    }

    public void setJobLink(String jobLink) {
        this.jobLink = jobLink;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
