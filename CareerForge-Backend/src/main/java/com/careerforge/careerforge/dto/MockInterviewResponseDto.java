package com.careerforge.careerforge.dto;

import com.careerforge.careerforge.entity.MockInterview;
import com.careerforge.careerforge.entity.MockInterviewStatus;
import com.careerforge.careerforge.entity.MockInterviewType;
import java.time.LocalDateTime;

public class MockInterviewResponseDto {

    private Long id;
    private String title;
    private MockInterviewType interviewType;
    private String targetCompany;
    private String targetRole;
    private LocalDateTime scheduledDate;
    private Integer durationMinutes;
    private MockInterviewStatus status;
    private Integer rating;
    private String feedback;
    private String strengths;
    private String areasOfImprovement;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MockInterviewResponseDto() {
    }

    public MockInterviewResponseDto(MockInterview entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.interviewType = entity.getInterviewType();
        this.targetCompany = entity.getTargetCompany();
        this.targetRole = entity.getTargetRole();
        this.scheduledDate = entity.getScheduledDate();
        this.durationMinutes = entity.getDurationMinutes();
        this.status = entity.getStatus();
        this.rating = entity.getRating();
        this.feedback = entity.getFeedback();
        this.strengths = entity.getStrengths();
        this.areasOfImprovement = entity.getAreasOfImprovement();
        this.notes = entity.getNotes();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MockInterviewType getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(MockInterviewType interviewType) {
        this.interviewType = interviewType;
    }

    public String getTargetCompany() {
        return targetCompany;
    }

    public void setTargetCompany(String targetCompany) {
        this.targetCompany = targetCompany;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public MockInterviewStatus getStatus() {
        return status;
    }

    public void setStatus(MockInterviewStatus status) {
        this.status = status;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getAreasOfImprovement() {
        return areasOfImprovement;
    }

    public void setAreasOfImprovement(String areasOfImprovement) {
        this.areasOfImprovement = areasOfImprovement;
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
