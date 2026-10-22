package com.careerforge.careerforge.service;

import com.careerforge.careerforge.dto.AnalyticsOverviewDto;
import com.careerforge.careerforge.entity.*;
import com.careerforge.careerforge.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AnalyticsService {

    private final DsaProblemRepository dsaProblemRepository;
    private final AptitudePracticeSessionRepository aptitudeSessionRepository;
    private final ResumeRepository resumeRepository;
    private final CertificationRepository certificationRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final MockInterviewRepository mockInterviewRepository;

    public AnalyticsService(
            DsaProblemRepository dsaProblemRepository,
            AptitudePracticeSessionRepository aptitudeSessionRepository,
            ResumeRepository resumeRepository,
            CertificationRepository certificationRepository,
            JobApplicationRepository jobApplicationRepository,
            MockInterviewRepository mockInterviewRepository) {
        this.dsaProblemRepository = dsaProblemRepository;
        this.aptitudeSessionRepository = aptitudeSessionRepository;
        this.resumeRepository = resumeRepository;
        this.certificationRepository = certificationRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.mockInterviewRepository = mockInterviewRepository;
    }

    @Transactional(readOnly = true)
    public AnalyticsOverviewDto getOverview(User user) {
        AnalyticsOverviewDto overview = new AnalyticsOverviewDto();

        // 1. DSA Analytics
        long dsaTotal = dsaProblemRepository.countByUser(user);
        long dsaSolved = dsaProblemRepository.countByUserAndStatus(user, Status.SOLVED);
        long dsaEasySolved = dsaProblemRepository.findByUser(user).stream()
                .filter(p -> p.getDifficulty() == Difficulty.EASY && p.getStatus() == Status.SOLVED).count();
        long dsaMediumSolved = dsaProblemRepository.findByUser(user).stream()
                .filter(p -> p.getDifficulty() == Difficulty.MEDIUM && p.getStatus() == Status.SOLVED).count();
        long dsaHardSolved = dsaProblemRepository.findByUser(user).stream()
                .filter(p -> p.getDifficulty() == Difficulty.HARD && p.getStatus() == Status.SOLVED).count();
        double dsaCompletionRate = dsaTotal > 0 ? Math.round(((double) dsaSolved / dsaTotal * 100.0) * 10.0) / 10.0 : 0.0;
        overview.setDsa(new AnalyticsOverviewDto.DsaAnalyticsDto(dsaSolved, dsaTotal, dsaEasySolved, dsaMediumSolved, dsaHardSolved, dsaCompletionRate));

        // 2. Aptitude Analytics
        List<AptitudePracticeSession> aptSessions = aptitudeSessionRepository.findByUserAndCompletedTrueOrderByCompletedAtDesc(user);
        long aptSessionsCount = aptSessions.size();
        long aptAttempted = aptSessions.stream().mapToLong(AptitudePracticeSession::getTotalQuestions).sum();
        long aptCorrect = aptSessions.stream().mapToLong(AptitudePracticeSession::getCorrectAnswers).sum();
        double aptAccuracy = aptAttempted > 0 ? Math.round(((double) aptCorrect / aptAttempted * 100.0) * 10.0) / 10.0 : 0.0;
        overview.setAptitude(new AnalyticsOverviewDto.AptitudeAnalyticsDto(aptSessionsCount, aptAttempted, aptCorrect, aptAccuracy));

        // 3. Resume Analytics
        List<Resume> resumes = resumeRepository.findByUserOrderByIsPrimaryDescUpdatedAtDesc(user);
        long totalResumes = resumes.size();
        Optional<Resume> primaryResume = resumes.stream().filter(Resume::isPrimary).findFirst();
        boolean hasPrimary = primaryResume.isPresent();
        String primaryName = primaryResume.map(Resume::getResumeName).orElse(null);
        overview.setResume(new AnalyticsOverviewDto.ResumeAnalyticsDto(totalResumes, hasPrimary, primaryName));

        // 4. Certifications Analytics
        long totalCerts = certificationRepository.countByUser(user);
        overview.setCertifications(new AnalyticsOverviewDto.CertificationsAnalyticsDto(totalCerts));

        // 5. Applications Pipeline Analytics
        long totalApps = jobApplicationRepository.countByUser(user);
        long appliedApps = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.APPLIED);
        long oaApps = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.ONLINE_ASSESSMENT);
        long interviewApps = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.INTERVIEW);
        long offerApps = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.OFFER);
        long rejectedApps = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.REJECTED);
        double offerRate = totalApps > 0 ? Math.round(((double) offerApps / totalApps * 100.0) * 10.0) / 10.0 : 0.0;
        overview.setApplications(new AnalyticsOverviewDto.ApplicationsAnalyticsDto(totalApps, appliedApps, oaApps, interviewApps, offerApps, rejectedApps, offerRate));

        // 6. Mock Interviews Analytics
        long totalInterviews = mockInterviewRepository.countByUser(user);
        long completedInterviews = mockInterviewRepository.countByUserAndStatus(user, MockInterviewStatus.COMPLETED);
        Double avgRating = mockInterviewRepository.calculateAverageRatingByUser(user);
        double interviewAvg = avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0;
        overview.setMockInterviews(new AnalyticsOverviewDto.MockInterviewsAnalyticsDto(totalInterviews, completedInterviews, interviewAvg));

        // 7. Dimension Score Calculations (0-100)
        double dsaScore = Math.min(100.0, dsaSolved * 5.0); // 20 solved = 100%
        double aptScore = aptAttempted > 0 ? aptAccuracy : 0.0;
        double resumeScore = hasPrimary ? 100.0 : (totalResumes > 0 ? 60.0 : 0.0);
        double certScore = Math.min(100.0, totalCerts * 35.0); // 3 certs = 100%
        double interviewScore = completedInterviews > 0 ? Math.min(100.0, interviewAvg * 10.0) : 0.0;
        double appScore = Math.min(100.0, totalApps * 20.0); // 5 applications = 100%

        Map<String, Double> categoryScores = new LinkedHashMap<>();
        categoryScores.put("DSA Problem Solving", Math.round(dsaScore * 10.0) / 10.0);
        categoryScores.put("Aptitude Accuracy", Math.round(aptScore * 10.0) / 10.0);
        categoryScores.put("Resume Readiness", Math.round(resumeScore * 10.0) / 10.0);
        categoryScores.put("Certifications", Math.round(certScore * 10.0) / 10.0);
        categoryScores.put("Mock Interviews", Math.round(interviewScore * 10.0) / 10.0);
        categoryScores.put("Job Applications", Math.round(appScore * 10.0) / 10.0);
        overview.setCategoryScores(categoryScores);

        // Composite Weighted Readiness Score:
        // DSA (25%), Aptitude (20%), Resume (15%), Mock Interviews (15%), Certifications (15%), Applications (10%)
        double compositeScore = (dsaScore * 0.25)
                + (aptScore * 0.20)
                + (resumeScore * 0.15)
                + (interviewScore * 0.15)
                + (certScore * 0.15)
                + (appScore * 0.10);
        overview.setReadinessScore(Math.round(compositeScore * 10.0) / 10.0);

        // Dynamic Recommendations
        List<String> recommendations = new ArrayList<>();
        if (dsaSolved < 10) {
            recommendations.add("Solve more DSA problems to build algorithmic consistency across Array, DP, and Graph patterns.");
        }
        if (aptAttempted < 15 || aptAccuracy < 70) {
            recommendations.add("Engage in timed Quantitative and Logical aptitude drills to boost test speed and accuracy.");
        }
        if (!hasPrimary) {
            recommendations.add("Upload and designate a primary ATS-friendly resume to accelerate application readiness.");
        }
        if (completedInterviews < 2) {
            recommendations.add("Schedule or rehearse at least 2 mock interview rounds with live feedback to hone behavioral fluency.");
        }
        if (totalCerts < 1) {
            recommendations.add("Add verified industry certifications to substantiate your hands-on competencies.");
        }
        if (totalApps < 3) {
            recommendations.add("Expand your job search: apply to at least 3 active campus or off-campus openings.");
        }
        if (recommendations.isEmpty()) {
            recommendations.add("Outstanding readiness! Maintain daily problem-solving streaks and stay prepared for upcoming interviews.");
        }
        overview.setRecommendations(recommendations);

        return overview;
    }
}
