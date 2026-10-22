package com.careerforge.careerforge.dto;

import com.careerforge.careerforge.entity.AptitudeCategory;
import com.careerforge.careerforge.entity.Difficulty;

import java.time.LocalDateTime;
import java.util.List;

public class AptitudeSessionResultsDto {

    private Long sessionId;
    private AptitudeCategory category;
    private String categoryDisplayName;
    private String topicFilter;
    private Difficulty difficultyFilter;
    private int totalQuestions;
    private int attemptedQuestions;
    private int correctAnswers;
    private int incorrectAnswers;
    private double accuracyPercentage;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private List<AptitudeQuestionReviewDto> questionReviews;

    public AptitudeSessionResultsDto() {
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public AptitudeCategory getCategory() {
        return category;
    }

    public void setCategory(AptitudeCategory category) {
        this.category = category;
        if (category != null) {
            this.categoryDisplayName = category.getDisplayName();
        }
    }

    public String getCategoryDisplayName() {
        return categoryDisplayName;
    }

    public void setCategoryDisplayName(String categoryDisplayName) {
        this.categoryDisplayName = categoryDisplayName;
    }

    public String getTopicFilter() {
        return topicFilter;
    }

    public void setTopicFilter(String topicFilter) {
        this.topicFilter = topicFilter;
    }

    public Difficulty getDifficultyFilter() {
        return difficultyFilter;
    }

    public void setDifficultyFilter(Difficulty difficultyFilter) {
        this.difficultyFilter = difficultyFilter;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getAttemptedQuestions() {
        return attemptedQuestions;
    }

    public void setAttemptedQuestions(int attemptedQuestions) {
        this.attemptedQuestions = attemptedQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getIncorrectAnswers() {
        return incorrectAnswers;
    }

    public void setIncorrectAnswers(int incorrectAnswers) {
        this.incorrectAnswers = incorrectAnswers;
    }

    public double getAccuracyPercentage() {
        return accuracyPercentage;
    }

    public void setAccuracyPercentage(double accuracyPercentage) {
        this.accuracyPercentage = accuracyPercentage;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public List<AptitudeQuestionReviewDto> getQuestionReviews() {
        return questionReviews;
    }

    public void setQuestionReviews(List<AptitudeQuestionReviewDto> questionReviews) {
        this.questionReviews = questionReviews;
    }
}
