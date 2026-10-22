package com.careerforge.careerforge.dto;

import java.util.Map;

public class MockInterviewStatsDto {

    private long totalInterviews;
    private long completedCount;
    private long scheduledCount;
    private long cancelledCount;
    private double averageRating;
    private Map<String, Long> typeDistribution;

    public MockInterviewStatsDto() {
    }

    public MockInterviewStatsDto(long totalInterviews,
                                long completedCount,
                                long scheduledCount,
                                long cancelledCount,
                                double averageRating,
                                Map<String, Long> typeDistribution) {
        this.totalInterviews = totalInterviews;
        this.completedCount = completedCount;
        this.scheduledCount = scheduledCount;
        this.cancelledCount = cancelledCount;
        this.averageRating = averageRating;
        this.typeDistribution = typeDistribution;
    }

    public long getTotalInterviews() {
        return totalInterviews;
    }

    public void setTotalInterviews(long totalInterviews) {
        this.totalInterviews = totalInterviews;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
    }

    public long getScheduledCount() {
        return scheduledCount;
    }

    public void setScheduledCount(long scheduledCount) {
        this.scheduledCount = scheduledCount;
    }

    public long getCancelledCount() {
        return cancelledCount;
    }

    public void setCancelledCount(long cancelledCount) {
        this.cancelledCount = cancelledCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public Map<String, Long> getTypeDistribution() {
        return typeDistribution;
    }

    public void setTypeDistribution(Map<String, Long> typeDistribution) {
        this.typeDistribution = typeDistribution;
    }
}
