package com.careerforge.careerforge.dto;

import com.careerforge.careerforge.entity.AptitudeCategory;

import java.util.List;

public class CategoryTopicsDto {

    private AptitudeCategory category;
    private String displayName;
    private List<String> topics;

    public CategoryTopicsDto() {
    }

    public CategoryTopicsDto(AptitudeCategory category, String displayName, List<String> topics) {
        this.category = category;
        this.displayName = displayName;
        this.topics = topics;
    }

    public AptitudeCategory getCategory() {
        return category;
    }

    public void setCategory(AptitudeCategory category) {
        this.category = category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }
}
