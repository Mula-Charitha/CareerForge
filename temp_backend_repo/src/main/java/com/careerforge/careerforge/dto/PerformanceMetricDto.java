package com.careerforge.careerforge.dto;

public class PerformanceMetricDto {

    private String name;
    private long totalQuestions;
    private long attempted;
    private long correct;
    private double accuracy;

    public PerformanceMetricDto() {
    }

    public PerformanceMetricDto(String name, long totalQuestions, long attempted, long correct, double accuracy) {
        this.name = name;
        this.totalQuestions = totalQuestions;
        this.attempted = attempted;
        this.correct = correct;
        this.accuracy = accuracy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public long getAttempted() {
        return attempted;
    }

    public void setAttempted(long attempted) {
        this.attempted = attempted;
    }

    public long getCorrect() {
        return correct;
    }

    public void setCorrect(long correct) {
        this.correct = correct;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}
