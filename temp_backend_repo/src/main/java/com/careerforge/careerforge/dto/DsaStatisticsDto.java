package com.careerforge.careerforge.dto;

import java.util.List;

public class DsaStatisticsDto {

    private long totalProblems;
    private long solvedProblems;
    private long attemptedProblems;
    private long notStartedProblems;
    private long easyProblems;
    private long mediumProblems;
    private long hardProblems;
    private int overallProgress;
    private List<TopicProgressDto> topicWiseProgress;
    private List<String> topics;

    public DsaStatisticsDto() {
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

    public long getEasyProblems() {
        return easyProblems;
    }

    public void setEasyProblems(long easyProblems) {
        this.easyProblems = easyProblems;
    }

    public long getMediumProblems() {
        return mediumProblems;
    }

    public void setMediumProblems(long mediumProblems) {
        this.mediumProblems = mediumProblems;
    }

    public long getHardProblems() {
        return hardProblems;
    }

    public void setHardProblems(long hardProblems) {
        this.hardProblems = hardProblems;
    }

    public int getOverallProgress() {
        return overallProgress;
    }

    public void setOverallProgress(int overallProgress) {
        this.overallProgress = overallProgress;
    }

    public List<TopicProgressDto> getTopicWiseProgress() {
        return topicWiseProgress;
    }

    public void setTopicWiseProgress(List<TopicProgressDto> topicWiseProgress) {
        this.topicWiseProgress = topicWiseProgress;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
