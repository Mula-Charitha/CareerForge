package com.careerforge.careerforge.dto;

public class TopicProgressDto {

    private String topic;
    private long totalProblems;
    private long solvedProblems;
    private long attemptedProblems;
    private long notStartedProblems;
    private int completionPercentage;

    public TopicProgressDto() {
    }

    public TopicProgressDto(String topic, long totalProblems, long solvedProblems, long attemptedProblems, long notStartedProblems, int completionPercentage) {
        this.topic = topic;
        this.totalProblems = totalProblems;
        this.solvedProblems = solvedProblems;
        this.attemptedProblems = attemptedProblems;
        this.notStartedProblems = notStartedProblems;
        this.completionPercentage = completionPercentage;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getTotalProblems() {
        return totalProblems;
    }

    public void setTotalProblems(long totalProblems) {
        this.totalProblems = totalProblems;
    }

    public long getSolvedProblems() {
        return solvedProblems;
    }

    public void setSolvedProblems(long solvedProblems) {
        this.solvedProblems = solvedProblems;
    }

    public long getAttemptedProblems() {
        return attemptedProblems;
    }

    public void setAttemptedProblems(long attemptedProblems) {
        this.attemptedProblems = attemptedProblems;
    }

    public long getNotStartedProblems() {
        return notStartedProblems;
    }

    public void setNotStartedProblems(long notStartedProblems) {
        this.notStartedProblems = notStartedProblems;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = completionPercentage;
    }
}
