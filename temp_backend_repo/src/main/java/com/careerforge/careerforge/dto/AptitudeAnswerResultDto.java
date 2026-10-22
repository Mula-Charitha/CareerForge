package com.careerforge.careerforge.dto;

public class AptitudeAnswerResultDto {

    private Long questionId;
    private String selectedOption;
    private boolean isCorrect;
    private String correctOption;
    private String explanation;
    private int attemptedQuestions;
    private int correctAnswers;
    private int totalQuestions;
    private double currentAccuracy;

    public AptitudeAnswerResultDto() {
    }

    public AptitudeAnswerResultDto(Long questionId, String selectedOption, boolean isCorrect,
                                  String correctOption, String explanation,
                                  int attemptedQuestions, int correctAnswers, int totalQuestions,
                                  double currentAccuracy) {
        this.questionId = questionId;
        this.selectedOption = selectedOption;
        this.isCorrect = isCorrect;
        this.correctOption = correctOption;
        this.explanation = explanation;
        this.attemptedQuestions = attemptedQuestions;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.currentAccuracy = currentAccuracy;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
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

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public double getCurrentAccuracy() {
        return currentAccuracy;
    }

    public void setCurrentAccuracy(double currentAccuracy) {
        this.currentAccuracy = currentAccuracy;
    }
}
