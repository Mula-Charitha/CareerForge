package com.careerforge.careerforge.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "aptitude_answers",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"session_id", "question_id"})
    }
)
public class AptitudeAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private AptitudePracticeSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private AptitudeQuestion question;

    @Column(name = "selected_option", length = 5, nullable = false)
    private String selectedOption; // "A", "B", "C", "D"

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @Column(name = "answered_at", nullable = false, updatable = false)
    private LocalDateTime answeredAt;

    @PrePersist
    protected void onCreate() {
        if (this.answeredAt == null) {
            this.answeredAt = LocalDateTime.now();
        }
    }

    public AptitudeAnswer() {
    }

    public AptitudeAnswer(AptitudePracticeSession session, AptitudeQuestion question, String selectedOption, boolean isCorrect) {
        this.session = session;
        this.question = question;
        this.selectedOption = selectedOption;
        this.isCorrect = isCorrect;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AptitudePracticeSession getSession() {
        return session;
    }

    public void setSession(AptitudePracticeSession session) {
        this.session = session;
    }

    public AptitudeQuestion getQuestion() {
        return question;
    }

    public void setQuestion(AptitudeQuestion question) {
        this.question = question;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }
}
