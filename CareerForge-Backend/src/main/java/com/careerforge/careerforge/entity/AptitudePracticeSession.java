package com.careerforge.careerforge.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aptitude_practice_sessions")
public class AptitudePracticeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private AptitudeCategory category;

    @Column(name = "topic_filter")
    private String topicFilter;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_filter")
    private Difficulty difficultyFilter;

    @Column(name = "total_questions", nullable = false)
    private int totalQuestions = 0;

    @Column(name = "attempted_questions", nullable = false)
    private int attemptedQuestions = 0;

    @Column(name = "correct_answers", nullable = false)
    private int correctAnswers = 0;

    @Column(name = "incorrect_answers", nullable = false)
    private int incorrectAnswers = 0;

    @Column(name = "accuracy_percentage", nullable = false)
    private double accuracyPercentage = 0.0;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    @Column(name = "started_at", nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AptitudeAnswer> answers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "aptitude_session_questions",
        joinColumns = @JoinColumn(name = "session_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<AptitudeQuestion> questions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.startedAt == null) {
            this.startedAt = LocalDateTime.now();
        }
    }

    public AptitudePracticeSession() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AptitudeCategory getCategory() {
        return category;
    }

    public void setCategory(AptitudeCategory category) {
        this.category = category;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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

    public List<AptitudeAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AptitudeAnswer> answers) {
        this.answers = answers;
    }

    public List<AptitudeQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<AptitudeQuestion> questions) {
        this.questions = questions;
    }
}
