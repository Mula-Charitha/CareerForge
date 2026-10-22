package com.careerforge.careerforge.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AIChatResponseDto {

    private Long id;
    private String userPrompt;
    private String aiResponse;
    private String category;
    private List<String> suggestions;
    private LocalDateTime createdAt;

    public AIChatResponseDto() {
    }

    public AIChatResponseDto(Long id, String userPrompt, String aiResponse, String category, List<String> suggestions, LocalDateTime createdAt) {
        this.id = id;
        this.userPrompt = userPrompt;
        this.aiResponse = aiResponse;
        this.category = category;
        this.suggestions = suggestions;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserPrompt() {
        return userPrompt;
    }

    public void setUserPrompt(String userPrompt) {
        this.userPrompt = userPrompt;
    }

    public String getAiResponse() {
        return aiResponse;
    }

    public void setAiResponse(String aiResponse) {
        this.aiResponse = aiResponse;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<String> suggestions) {
        this.suggestions = suggestions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
