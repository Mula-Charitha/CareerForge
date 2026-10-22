package com.careerforge.careerforge.service;

import com.careerforge.careerforge.dto.*;
import com.careerforge.careerforge.entity.*;
import com.careerforge.careerforge.repository.AptitudeAnswerRepository;
import com.careerforge.careerforge.repository.AptitudePracticeSessionRepository;
import com.careerforge.careerforge.repository.AptitudeQuestionRepository;
import com.careerforge.careerforge.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AptitudeService {

    private final AptitudeQuestionRepository questionRepository;
    private final AptitudePracticeSessionRepository sessionRepository;
    private final AptitudeAnswerRepository answerRepository;
    private final UserRepository userRepository;

    public AptitudeService(AptitudeQuestionRepository questionRepository,
                           AptitudePracticeSessionRepository sessionRepository,
                           AptitudeAnswerRepository answerRepository,
                           UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.sessionRepository = sessionRepository;
        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryTopicsDto> getCategories() {
        return Arrays.stream(AptitudeCategory.values())
                .map(cat -> new CategoryTopicsDto(cat, cat.getDisplayName(), cat.getTopics()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getTopicsByCategory(AptitudeCategory category) {
        if (category == null) {
            return Collections.emptyList();
        }
        return category.getTopics();
    }

    @Transactional(readOnly = true)
    public List<AptitudeQuestionDto> getQuestions(AptitudeCategory category, String topic, Difficulty difficulty) {
        List<AptitudeQuestion> questions = questionRepository.findFilteredQuestions(category, topic, difficulty);
        return questions.stream()
                .map(this::maskQuestionToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AptitudeSessionResponseDto createSession(String email, AptitudeSessionRequestDto request) {
        User user = getUserByEmail(email);

        List<AptitudeQuestion> candidateQuestions = questionRepository.findFilteredQuestions(
                request.getCategory(),
                request.getTopic(),
                request.getDifficulty()
        );

        if (candidateQuestions.isEmpty()) {
            throw new IllegalArgumentException(
                    "No questions available in the question bank matching the selected criteria. " +
                    "Try choosing all topics or a different difficulty."
            );
        }

        // Shuffle questions to provide randomized practice
        List<AptitudeQuestion> shuffled = new ArrayList<>(candidateQuestions);
        Collections.shuffle(shuffled);

        int limit = Math.min(request.getNumberOfQuestions(), shuffled.size());
        List<AptitudeQuestion> selectedQuestions = shuffled.subList(0, limit);

        AptitudePracticeSession session = new AptitudePracticeSession();
        session.setUser(user);
        session.setCategory(request.getCategory());
        session.setTopicFilter(request.getTopic() != null && !request.getTopic().isBlank() ? request.getTopic().trim() : null);
        session.setDifficultyFilter(request.getDifficulty());
        session.setTotalQuestions(selectedQuestions.size());
        session.setAttemptedQuestions(0);
        session.setCorrectAnswers(0);
        session.setIncorrectAnswers(0);
        session.setAccuracyPercentage(0.0);
        session.setCompleted(false);
        session.setStartedAt(LocalDateTime.now());
        session.setQuestions(new ArrayList<>(selectedQuestions));

        AptitudePracticeSession saved = sessionRepository.save(session);
        return mapToSessionResponse(saved);
    }

    @Transactional(readOnly = true)
    public AptitudeSessionResponseDto getSessionQuestions(String email, Long sessionId) {
        User user = getUserByEmail(email);
        AptitudePracticeSession session = getSessionForUser(sessionId, user);
        return mapToSessionResponse(session);
    }

    @Transactional
    public AptitudeAnswerResultDto submitAnswer(String email, Long sessionId, AptitudeAnswerRequestDto request) {
        User user = getUserByEmail(email);
        AptitudePracticeSession session = getSessionForUser(sessionId, user);

        if (session.isCompleted()) {
            throw new IllegalStateException("This practice session has already been completed.");
        }

        AptitudeQuestion question = session.getQuestions().stream()
                .filter(q -> q.getId().equals(request.getQuestionId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Question with id " + request.getQuestionId() + " does not belong to this practice session"));

        if (answerRepository.existsBySessionAndQuestion(session, question)) {
            throw new IllegalStateException("You have already answered this question in this session.");
        }

        String normalizedSelected = request.getSelectedOption().trim().toUpperCase();
        boolean isCorrect = question.getCorrectOption().equalsIgnoreCase(normalizedSelected);

        AptitudeAnswer answer = new AptitudeAnswer(session, question, normalizedSelected, isCorrect);
        answerRepository.save(answer);

        // Update session progress
        int newAttempted = session.getAttemptedQuestions() + 1;
        int newCorrect = session.getCorrectAnswers() + (isCorrect ? 1 : 0);
        int newIncorrect = session.getIncorrectAnswers() + (isCorrect ? 0 : 1);
        double newAccuracy = newAttempted > 0 ? ((double) newCorrect / newAttempted) * 100.0 : 0.0;

        session.setAttemptedQuestions(newAttempted);
        session.setCorrectAnswers(newCorrect);
        session.setIncorrectAnswers(newIncorrect);
        session.setAccuracyPercentage(Math.round(newAccuracy * 10.0) / 10.0);

        if (newAttempted >= session.getTotalQuestions()) {
            session.setCompleted(true);
            session.setCompletedAt(LocalDateTime.now());
        }

        sessionRepository.save(session);

        return new AptitudeAnswerResultDto(
                question.getId(),
                normalizedSelected,
                isCorrect,
                question.getCorrectOption(),
                question.getExplanation(),
                newAttempted,
                newCorrect,
                session.getTotalQuestions(),
                session.getAccuracyPercentage()
        );
    }

    @Transactional
    public AptitudeSessionResultsDto completeSession(String email, Long sessionId) {
        User user = getUserByEmail(email);
        AptitudePracticeSession session = getSessionForUser(sessionId, user);

        if (!session.isCompleted()) {
            session.setCompleted(true);
            session.setCompletedAt(LocalDateTime.now());
            sessionRepository.save(session);
        }

        return buildSessionResults(session);
    }

    @Transactional(readOnly = true)
    public AptitudeSessionResultsDto getSessionResults(String email, Long sessionId) {
        User user = getUserByEmail(email);
        AptitudePracticeSession session = getSessionForUser(sessionId, user);
        return buildSessionResults(session);
    }

    @Transactional(readOnly = true)
    public List<AptitudeSessionHistoryDto> getSessionHistory(String email) {
        User user = getUserByEmail(email);
        List<AptitudePracticeSession> sessions = sessionRepository.findByUserOrderByStartedAtDesc(user);

        return sessions.stream().map(s -> {
            AptitudeSessionHistoryDto dto = new AptitudeSessionHistoryDto();
            dto.setId(s.getId());
            dto.setCategory(s.getCategory());
            dto.setTopicFilter(s.getTopicFilter());
            dto.setDifficultyFilter(s.getDifficultyFilter());
            dto.setTotalQuestions(s.getTotalQuestions());
            dto.setAttemptedQuestions(s.getAttemptedQuestions());
            dto.setCorrectAnswers(s.getCorrectAnswers());
            dto.setIncorrectAnswers(s.getIncorrectAnswers());
            dto.setAccuracyPercentage(s.getAccuracyPercentage());
            dto.setCompleted(s.isCompleted());
            dto.setStartedAt(s.getStartedAt());
            dto.setCompletedAt(s.getCompletedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AptitudeStatisticsDto getStatistics(String email) {
        User user = getUserByEmail(email);

        long totalQuestionsInBank = questionRepository.count();
        long totalAttempted = answerRepository.countBySession_User(user);
        long totalCorrect = answerRepository.countBySession_UserAndIsCorrectTrue(user);
        long totalIncorrect = answerRepository.countBySession_UserAndIsCorrectFalse(user);

        double overallAccuracy = totalAttempted > 0
                ? Math.round(((double) totalCorrect / totalAttempted) * 1000.0) / 10.0
                : 0.0;

        long completedSessions = sessionRepository.countByUserAndCompletedTrue(user);

        // Category breakdown
        List<PerformanceMetricDto> categoryPerformance = new ArrayList<>();
        double quantAccuracy = 0.0;
        double logAccuracy = 0.0;
        double verbAccuracy = 0.0;

        for (AptitudeCategory cat : AptitudeCategory.values()) {
            long cTotal = questionRepository.countByCategory(cat);
            long cAttempted = answerRepository.countBySession_UserAndQuestion_Category(user, cat);
            long cCorrect = answerRepository.countBySession_UserAndQuestion_CategoryAndIsCorrectTrue(user, cat);
            double cAcc = cAttempted > 0 ? Math.round(((double) cCorrect / cAttempted) * 1000.0) / 10.0 : 0.0;

            categoryPerformance.add(new PerformanceMetricDto(cat.getDisplayName(), cTotal, cAttempted, cCorrect, cAcc));

            if (cat == AptitudeCategory.QUANTITATIVE) quantAccuracy = cAcc;
            else if (cat == AptitudeCategory.LOGICAL) logAccuracy = cAcc;
            else if (cat == AptitudeCategory.VERBAL) verbAccuracy = cAcc;
        }

        // Topic breakdown
        List<PerformanceMetricDto> topicPerformance = new ArrayList<>();
        for (AptitudeCategory cat : AptitudeCategory.values()) {
            for (String topic : cat.getTopics()) {
                long tTotal = questionRepository.countByCategoryAndTopic(cat, topic);
                long tAttempted = answerRepository.countBySession_UserAndQuestion_Topic(user, topic);
                long tCorrect = answerRepository.countBySession_UserAndQuestion_TopicAndIsCorrectTrue(user, topic);
                double tAcc = tAttempted > 0 ? Math.round(((double) tCorrect / tAttempted) * 1000.0) / 10.0 : 0.0;

                topicPerformance.add(new PerformanceMetricDto(topic, tTotal, tAttempted, tCorrect, tAcc));
            }
        }

        // Difficulty breakdown
        List<PerformanceMetricDto> difficultyPerformance = new ArrayList<>();
        for (Difficulty diff : Difficulty.values()) {
            long dAttempted = answerRepository.countBySession_UserAndQuestion_Difficulty(user, diff);
            long dCorrect = answerRepository.countBySession_UserAndQuestion_DifficultyAndIsCorrectTrue(user, diff);
            double dAcc = dAttempted > 0 ? Math.round(((double) dCorrect / dAttempted) * 1000.0) / 10.0 : 0.0;

            difficultyPerformance.add(new PerformanceMetricDto(diff.name(), 0, dAttempted, dCorrect, dAcc));
        }

        // Recent history (last 5 sessions)
        List<AptitudePracticeSession> recentList = sessionRepository.findByUserOrderByStartedAtDesc(user);
        List<AptitudeSessionHistoryDto> recentHistory = recentList.stream()
                .limit(5)
                .map(s -> {
                    AptitudeSessionHistoryDto h = new AptitudeSessionHistoryDto();
                    h.setId(s.getId());
                    h.setCategory(s.getCategory());
                    h.setTopicFilter(s.getTopicFilter());
                    h.setDifficultyFilter(s.getDifficultyFilter());
                    h.setTotalQuestions(s.getTotalQuestions());
                    h.setAttemptedQuestions(s.getAttemptedQuestions());
                    h.setCorrectAnswers(s.getCorrectAnswers());
                    h.setIncorrectAnswers(s.getIncorrectAnswers());
                    h.setAccuracyPercentage(s.getAccuracyPercentage());
                    h.setCompleted(s.isCompleted());
                    h.setStartedAt(s.getStartedAt());
                    h.setCompletedAt(s.getCompletedAt());
                    return h;
                })
                .collect(Collectors.toList());

        AptitudeStatisticsDto stats = new AptitudeStatisticsDto();
        stats.setTotalQuestionsInBank(totalQuestionsInBank);
        stats.setTotalQuestionsAttempted(totalAttempted);
        stats.setTotalCorrectAnswers(totalCorrect);
        stats.setTotalIncorrectAnswers(totalIncorrect);
        stats.setOverallAccuracyPercentage(overallAccuracy);
        stats.setCompletedSessions(completedSessions);
        stats.setQuantitativeAccuracy(quantAccuracy);
        stats.setLogicalAccuracy(logAccuracy);
        stats.setVerbalAccuracy(verbAccuracy);
        stats.setCategoryPerformance(categoryPerformance);
        stats.setTopicPerformance(topicPerformance);
        stats.setDifficultyPerformance(difficultyPerformance);
        stats.setRecentHistory(recentHistory);

        return stats;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    private AptitudePracticeSession getSessionForUser(Long sessionId, User user) {
        return sessionRepository.findByIdAndUser(sessionId, user)
                .orElseThrow(() -> new IllegalArgumentException("Aptitude practice session not found or does not belong to you with id: " + sessionId));
    }

    private AptitudeQuestionDto maskQuestionToDto(AptitudeQuestion question) {
        AptitudeQuestionDto dto = new AptitudeQuestionDto();
        dto.setId(question.getId());
        dto.setQuestionText(question.getQuestionText());
        dto.setCategory(question.getCategory());
        dto.setTopic(question.getTopic());
        dto.setDifficulty(question.getDifficulty());
        dto.setOptionA(question.getOptionA());
        dto.setOptionB(question.getOptionB());
        dto.setOptionC(question.getOptionC());
        dto.setOptionD(question.getOptionD());
        dto.setAnswered(false);
        // Do NOT expose correctOption or explanation!
        dto.setCorrectOption(null);
        dto.setExplanation(null);
        return dto;
    }

    private AptitudeSessionResponseDto mapToSessionResponse(AptitudePracticeSession session) {
        AptitudeSessionResponseDto dto = new AptitudeSessionResponseDto();
        dto.setSessionId(session.getId());
        dto.setCategory(session.getCategory());
        dto.setTopicFilter(session.getTopicFilter());
        dto.setDifficultyFilter(session.getDifficultyFilter());
        dto.setTotalQuestions(session.getTotalQuestions());
        dto.setAttemptedQuestions(session.getAttemptedQuestions());
        dto.setCorrectAnswers(session.getCorrectAnswers());
        dto.setIncorrectAnswers(session.getIncorrectAnswers());
        dto.setAccuracyPercentage(session.getAccuracyPercentage());
        dto.setCompleted(session.isCompleted());
        dto.setStartedAt(session.getStartedAt());
        dto.setCompletedAt(session.getCompletedAt());

        // Map session answers map
        List<AptitudeAnswer> answers = answerRepository.findBySession(session);
        Map<Long, AptitudeAnswer> answerMap = answers.stream()
                .collect(Collectors.toMap(a -> a.getQuestion().getId(), a -> a));

        List<AptitudeQuestionDto> questionDtos = session.getQuestions().stream().map(q -> {
            AptitudeQuestionDto qDto = new AptitudeQuestionDto();
            qDto.setId(q.getId());
            qDto.setQuestionText(q.getQuestionText());
            qDto.setCategory(q.getCategory());
            qDto.setTopic(q.getTopic());
            qDto.setDifficulty(q.getDifficulty());
            qDto.setOptionA(q.getOptionA());
            qDto.setOptionB(q.getOptionB());
            qDto.setOptionC(q.getOptionC());
            qDto.setOptionD(q.getOptionD());

            AptitudeAnswer existingAns = answerMap.get(q.getId());
            if (existingAns != null) {
                qDto.setAnswered(true);
                qDto.setSelectedOption(existingAns.getSelectedOption());
                qDto.setIsCorrect(existingAns.isCorrect());
                // Only revealed after being answered!
                qDto.setCorrectOption(q.getCorrectOption());
                qDto.setExplanation(q.getExplanation());
            } else {
                qDto.setAnswered(false);
                qDto.setSelectedOption(null);
                qDto.setIsCorrect(null);
                qDto.setCorrectOption(null);
                qDto.setExplanation(null);
            }
            return qDto;
        }).collect(Collectors.toList());

        dto.setQuestions(questionDtos);
        return dto;
    }

    private AptitudeSessionResultsDto buildSessionResults(AptitudePracticeSession session) {
        AptitudeSessionResultsDto dto = new AptitudeSessionResultsDto();
        dto.setSessionId(session.getId());
        dto.setCategory(session.getCategory());
        dto.setTopicFilter(session.getTopicFilter());
        dto.setDifficultyFilter(session.getDifficultyFilter());
        dto.setTotalQuestions(session.getTotalQuestions());
        dto.setAttemptedQuestions(session.getAttemptedQuestions());
        dto.setCorrectAnswers(session.getCorrectAnswers());
        dto.setIncorrectAnswers(session.getIncorrectAnswers());
        dto.setAccuracyPercentage(session.getAccuracyPercentage());
        dto.setStartedAt(session.getStartedAt());
        dto.setCompletedAt(session.getCompletedAt());

        List<AptitudeAnswer> answers = answerRepository.findBySession(session);
        Map<Long, AptitudeAnswer> answerMap = answers.stream()
                .collect(Collectors.toMap(a -> a.getQuestion().getId(), a -> a));

        List<AptitudeQuestionReviewDto> reviews = session.getQuestions().stream().map(q -> {
            AptitudeQuestionReviewDto r = new AptitudeQuestionReviewDto();
            r.setQuestionId(q.getId());
            r.setQuestionText(q.getQuestionText());
            r.setTopic(q.getTopic());
            r.setDifficulty(q.getDifficulty());
            r.setOptionA(q.getOptionA());
            r.setOptionB(q.getOptionB());
            r.setOptionC(q.getOptionC());
            r.setOptionD(q.getOptionD());
            r.setCorrectOption(q.getCorrectOption());
            r.setExplanation(q.getExplanation());

            AptitudeAnswer ans = answerMap.get(q.getId());
            if (ans != null) {
                r.setAttempted(true);
                r.setSelectedOption(ans.getSelectedOption());
                r.setCorrect(ans.isCorrect());
            } else {
                r.setAttempted(false);
                r.setSelectedOption(null);
                r.setCorrect(false);
            }
            return r;
        }).collect(Collectors.toList());

        dto.setQuestionReviews(reviews);
        return dto;
    }
}
