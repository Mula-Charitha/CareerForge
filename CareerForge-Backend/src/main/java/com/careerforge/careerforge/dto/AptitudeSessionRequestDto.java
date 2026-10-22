package com.careerforge.careerforge.dto;

import com.careerforge.careerforge.entity.AptitudeCategory;
import com.careerforge.careerforge.entity.Difficulty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AptitudeSessionRequestDto {

    @NotNull(message = "Category is required")
    private AptitudeCategory category;

    private String topic;

    private Difficulty difficulty;

    @Min(value = 1, message = "Minimum 1 question required")
    @Max(value = 50, message = "Maximum 50 questions allowed per session")
    private int numberOfQuestions = 10;

    public AptitudeSessionRequestDto() {
    }

    public AptitudeCategory getCategory() {
        return category;
    }

    public void setCategory(AptitudeCategory category) {
        this.category = category;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
