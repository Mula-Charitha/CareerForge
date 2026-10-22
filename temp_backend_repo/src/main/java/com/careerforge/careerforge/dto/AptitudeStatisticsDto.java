package com.careerforge.careerforge.dto;

import java.util.List;

public class AptitudeStatisticsDto {

    private long totalQuestionsInBank;
    private long totalQuestionsAttempted;
    private long totalCorrectAnswers;
    private long totalIncorrectAnswers;
    private double overallAccuracyPercentage;
    private long completedSessions;

    private double quantitativeAccuracy;
    private double logicalAccuracy;
    private double verbalAccuracy;

    private List<PerformanceMetricDto> categoryPerformance;
    private List<PerformanceMetricDto> topicPerformance;
    private List<PerformanceMetricDto> difficultyPerformance;
    private List<AptitudeSessionHistoryDto> recentHistory;

    public AptitudeStatisticsDto() {
    }

    public long getTotalQuestionsInBank() {
        return totalQuestionsInBank;
    }

    public void setTotalQuestionsInBank(long totalQuestionsInBank) {
        this.totalQuestionsInBank = totalQuestionsInBank;
    }

    public long getTotalQuestionsAttempted() {
        return totalQuestionsAttempted;
    }

    public void setTotalQuestionsAttempted(long totalQuestionsAttempted) {
        this.totalQuestionsAttempted = totalQuestionsAttempted;
    }

    public long getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    public void setTotalCorrectAnswers(long totalCorrectAnswers) {
        this.totalCorrectAnswers = totalCorrectAnswers;
    }

    public long getTotalIncorrectAnswers() {
        return totalIncorrectAnswers;
    }

    public void setTotalIncorrectAnswers(long totalIncorrectAnswers) {
        this.totalIncorrectAnswers = totalIncorrectAnswers;
    }

    public double getOverallAccuracyPercentage() {
        return overallAccuracyPercentage;
    }

    public void setOverallAccuracyPercentage(double overallAccuracyPercentage) {
        this.overallAccuracyPercentage = overallAccuracyPercentage;
    }

    public long getCompletedSessions() {
        return completedSessions;
    }

    public void setCompletedSessions(long completedSessions) {
        this.completedSessions = completedSessions;
    }

    public double getQuantitativeAccuracy() {
        return quantitativeAccuracy;
    }

    public void setQuantitativeAccuracy(double quantitativeAccuracy) {
        this.quantitativeAccuracy = quantitativeAccuracy;
    }

    public double getLogicalAccuracy() {
        return logicalAccuracy;
    }

    public void setLogicalAccuracy(double logicalAccuracy) {
        this.logicalAccuracy = logicalAccuracy;
    }

    public double getVerbalAccuracy() {
        return verbalAccuracy;
    }

    public void setVerbalAccuracy(double verbalAccuracy) {
        this.verbalAccuracy = verbalAccuracy;
    }

    public List<PerformanceMetricDto> getCategoryPerformance() {
        return categoryPerformance;
    }

    public void setCategoryPerformance(List<PerformanceMetricDto> categoryPerformance) {
        this.categoryPerformance = categoryPerformance;
    }

    public List<PerformanceMetricDto> getTopicPerformance() {
        return topicPerformance;
    }

    public void setTopicPerformance(List<PerformanceMetricDto> topicPerformance) {
        this.topicPerformance = topicPerformance;
    }

    public List<PerformanceMetricDto> getDifficultyPerformance() {
        return difficultyPerformance;
    }

    public void setDifficultyPerformance(List<PerformanceMetricDto> difficultyPerformance) {
        this.difficultyPerformance = difficultyPerformance;
    }

    public List<AptitudeSessionHistoryDto> getRecentHistory() {
        return recentHistory;
    }

    public void setRecentHistory(List<AptitudeSessionHistoryDto> recentHistory) {
        this.recentHistory = recentHistory;
    }
}
