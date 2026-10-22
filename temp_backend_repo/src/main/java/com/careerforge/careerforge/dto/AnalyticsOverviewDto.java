package com.careerforge.careerforge.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsOverviewDto {

    private double readinessScore;
    private Map<String, Double> categoryScores;
    private List<String> recommendations;

    private DsaAnalyticsDto dsa;
    private AptitudeAnalyticsDto aptitude;
    private ResumeAnalyticsDto resume;
    private CertificationsAnalyticsDto certifications;
    private ApplicationsAnalyticsDto applications;
    private MockInterviewsAnalyticsDto mockInterviews;

    public AnalyticsOverviewDto() {
    }

    public static class DsaAnalyticsDto {
        private long solved;
        private long total;
        private long easySolved;
        private long mediumSolved;
        private long hardSolved;
        private double completionRate;

        public DsaAnalyticsDto() {}

        public DsaAnalyticsDto(long solved, long total, long easySolved, long mediumSolved, long hardSolved, double completionRate) {
            this.solved = solved;
            this.total = total;
            this.easySolved = easySolved;
            this.mediumSolved = mediumSolved;
            this.hardSolved = hardSolved;
            this.completionRate = completionRate;
        }

        public long getSolved() { return solved; }
        public void setSolved(long solved) { this.solved = solved; }
        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }
        public long getEasySolved() { return easySolved; }
        public void setEasySolved(long easySolved) { this.easySolved = easySolved; }
        public long getMediumSolved() { return mediumSolved; }
        public void setMediumSolved(long mediumSolved) { this.mediumSolved = mediumSolved; }
        public long getHardSolved() { return hardSolved; }
        public void setHardSolved(long hardSolved) { this.hardSolved = hardSolved; }
        public double getCompletionRate() { return completionRate; }
        public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    }

    public static class AptitudeAnalyticsDto {
        private long sessionsCompleted;
        private long questionsAttempted;
        private long correctAnswers;
        private double accuracyPercentage;

        public AptitudeAnalyticsDto() {}

        public AptitudeAnalyticsDto(long sessionsCompleted, long questionsAttempted, long correctAnswers, double accuracyPercentage) {
            this.sessionsCompleted = sessionsCompleted;
            this.questionsAttempted = questionsAttempted;
            this.correctAnswers = correctAnswers;
            this.accuracyPercentage = accuracyPercentage;
        }

        public long getSessionsCompleted() { return sessionsCompleted; }
        public void setSessionsCompleted(long sessionsCompleted) { this.sessionsCompleted = sessionsCompleted; }
        public long getQuestionsAttempted() { return questionsAttempted; }
        public void setQuestionsAttempted(long questionsAttempted) { this.questionsAttempted = questionsAttempted; }
        public long getCorrectAnswers() { return correctAnswers; }
        public void setCorrectAnswers(long correctAnswers) { this.correctAnswers = correctAnswers; }
        public double getAccuracyPercentage() { return accuracyPercentage; }
        public void setAccuracyPercentage(double accuracyPercentage) { this.accuracyPercentage = accuracyPercentage; }
    }

    public static class ResumeAnalyticsDto {
        private long totalResumes;
        private boolean hasPrimary;
        private String primaryResumeName;

        public ResumeAnalyticsDto() {}

        public ResumeAnalyticsDto(long totalResumes, boolean hasPrimary, String primaryResumeName) {
            this.totalResumes = totalResumes;
            this.hasPrimary = hasPrimary;
            this.primaryResumeName = primaryResumeName;
        }

        public long getTotalResumes() { return totalResumes; }
        public void setTotalResumes(long totalResumes) { this.totalResumes = totalResumes; }
        public boolean isHasPrimary() { return hasPrimary; }
        public void setHasPrimary(boolean hasPrimary) { this.hasPrimary = hasPrimary; }
        public String getPrimaryResumeName() { return primaryResumeName; }
        public void setPrimaryResumeName(String primaryResumeName) { this.primaryResumeName = primaryResumeName; }
    }

    public static class CertificationsAnalyticsDto {
        private long totalCertifications;

        public CertificationsAnalyticsDto() {}

        public CertificationsAnalyticsDto(long totalCertifications) {
            this.totalCertifications = totalCertifications;
        }

        public long getTotalCertifications() { return totalCertifications; }
        public void setTotalCertifications(long totalCertifications) { this.totalCertifications = totalCertifications; }
    }

    public static class ApplicationsAnalyticsDto {
        private long totalApplications;
        private long appliedCount;
        private long onlineAssessmentCount;
        private long interviewCount;
        private long offerCount;
        private long rejectedCount;
        private double offerRate;

        public ApplicationsAnalyticsDto() {}

        public ApplicationsAnalyticsDto(long totalApplications, long appliedCount, long onlineAssessmentCount, long interviewCount, long offerCount, long rejectedCount, double offerRate) {
            this.totalApplications = totalApplications;
            this.appliedCount = appliedCount;
            this.onlineAssessmentCount = onlineAssessmentCount;
            this.interviewCount = interviewCount;
            this.offerCount = offerCount;
            this.rejectedCount = rejectedCount;
            this.offerRate = offerRate;
        }

        public long getTotalApplications() { return totalApplications; }
        public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }
        public long getAppliedCount() { return appliedCount; }
        public void setAppliedCount(long appliedCount) { this.appliedCount = appliedCount; }
        public long getOnlineAssessmentCount() { return onlineAssessmentCount; }
        public void setOnlineAssessmentCount(long onlineAssessmentCount) { this.onlineAssessmentCount = onlineAssessmentCount; }
        public long getInterviewCount() { return interviewCount; }
        public void setInterviewCount(long interviewCount) { this.interviewCount = interviewCount; }
        public long getOfferCount() { return offerCount; }
        public void setOfferCount(long offerCount) { this.offerCount = offerCount; }
        public long getRejectedCount() { return rejectedCount; }
        public void setRejectedCount(long rejectedCount) { this.rejectedCount = rejectedCount; }
        public double getOfferRate() { return offerRate; }
        public void setOfferRate(double offerRate) { this.offerRate = offerRate; }
    }

    public static class MockInterviewsAnalyticsDto {
        private long totalInterviews;
        private long completedCount;
        private double averageRating;

        public MockInterviewsAnalyticsDto() {}

        public MockInterviewsAnalyticsDto(long totalInterviews, long completedCount, double averageRating) {
            this.totalInterviews = totalInterviews;
            this.completedCount = completedCount;
            this.averageRating = averageRating;
        }

        public long getTotalInterviews() { return totalInterviews; }
        public void setTotalInterviews(long totalInterviews) { this.totalInterviews = totalInterviews; }
        public long getCompletedCount() { return completedCount; }
        public void setCompletedCount(long completedCount) { this.completedCount = completedCount; }
        public double getAverageRating() { return averageRating; }
        public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    }

    public double getReadinessScore() { return readinessScore; }
    public void setReadinessScore(double readinessScore) { this.readinessScore = readinessScore; }
    public Map<String, Double> getCategoryScores() { return categoryScores; }
    public void setCategoryScores(Map<String, Double> categoryScores) { this.categoryScores = categoryScores; }
    public List<String> getRecommendations() { return recommendations; }
    public void setRecommendations(List<String> recommendations) { this.recommendations = recommendations; }
    public DsaAnalyticsDto getDsa() { return dsa; }
    public void setDsa(DsaAnalyticsDto dsa) { this.dsa = dsa; }
    public AptitudeAnalyticsDto getAptitude() { return aptitude; }
    public void setAptitude(AptitudeAnalyticsDto aptitude) { this.aptitude = aptitude; }
    public ResumeAnalyticsDto getResume() { return resume; }
    public void setResume(ResumeAnalyticsDto resume) { this.resume = resume; }
    public CertificationsAnalyticsDto getCertifications() { return certifications; }
    public void setCertifications(CertificationsAnalyticsDto certifications) { this.certifications = certifications; }
    public ApplicationsAnalyticsDto getApplications() { return applications; }
    public void setApplications(ApplicationsAnalyticsDto applications) { this.applications = applications; }
    public MockInterviewsAnalyticsDto getMockInterviews() { return mockInterviews; }
    public void setMockInterviews(MockInterviewsAnalyticsDto mockInterviews) { this.mockInterviews = mockInterviews; }
}
