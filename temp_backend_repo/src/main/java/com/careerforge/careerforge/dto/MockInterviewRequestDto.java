package com.careerforge.careerforge.dto;

import com.careerforge.careerforge.entity.MockInterviewStatus;
import com.careerforge.careerforge.entity.MockInterviewType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class MockInterviewRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Interview type is required")
    private MockInterviewType interviewType = MockInterviewType.TECHNICAL;

    private String targetCompany;
    private String targetRole;

    @NotNull(message = "Scheduled date is required")
    private LocalDateTime scheduledDate;

    private Integer durationMinutes = 45;
    private MockInterviewStatus status = MockInterviewStatus.SCHEDULED;
    private Integer rating;
    private String feedback;
    private String strengths;
    private String areasOfImprovement;
    private String notes;

    public MockInterviewRequestDto() {
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
}
