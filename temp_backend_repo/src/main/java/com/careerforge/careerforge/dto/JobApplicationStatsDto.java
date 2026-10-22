package com.careerforge.careerforge.dto;

import java.util.Map;

public class JobApplicationStatsDto {

    private long totalApplications;
    private long appliedCount;
    private long onlineAssessmentCount;
    private long interviewCount;
    private long offerCount;
    private long rejectedCount;
    private long savedCount;
    private long withdrawnCount;
    private Map<String, Long> statusDistribution;

    public JobApplicationStatsDto() {
    }

    public JobApplicationStatsDto(long totalApplications,
                                 long appliedCount,
                                 long onlineAssessmentCount,
                                 long interviewCount,
                                 long offerCount,
                                 long rejectedCount,
                                 long savedCount,
                                 long withdrawnCount,
                                 Map<String, Long> statusDistribution) {
        this.totalApplications = totalApplications;
        this.appliedCount = appliedCount;
        this.onlineAssessmentCount = onlineAssessmentCount;
        this.interviewCount = interviewCount;
        this.offerCount = offerCount;
        this.rejectedCount = rejectedCount;
        this.savedCount = savedCount;
        this.withdrawnCount = withdrawnCount;
        this.statusDistribution = statusDistribution;
    }

    public long getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(long totalApplications) {
        this.totalApplications = totalApplications;
    }

    public long getAppliedCount() {
        return appliedCount;
    }

    public void setAppliedCount(long appliedCount) {
        this.appliedCount = appliedCount;
    }

    public long getOnlineAssessmentCount() {
        return onlineAssessmentCount;
    }

    public void setOnlineAssessmentCount(long onlineAssessmentCount) {
        this.onlineAssessmentCount = onlineAssessmentCount;
    }

    public long getInterviewCount() {
        return interviewCount;
    }

    public void setInterviewCount(long interviewCount) {
        this.interviewCount = interviewCount;
    }

    public long getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(long offerCount) {
        this.offerCount = offerCount;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public void setRejectedCount(long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public long getSavedCount() {
        return savedCount;
    }

    public void setSavedCount(long savedCount) {
        this.savedCount = savedCount;
    }

    public long getWithdrawnCount() {
        return withdrawnCount;
    }

    public void setWithdrawnCount(long withdrawnCount) {
        this.withdrawnCount = withdrawnCount;
    }

    public Map<String, Long> getStatusDistribution() {
        return statusDistribution;
    }

    public void setStatusDistribution(Map<String, Long> statusDistribution) {
        this.statusDistribution = statusDistribution;
    }
}
