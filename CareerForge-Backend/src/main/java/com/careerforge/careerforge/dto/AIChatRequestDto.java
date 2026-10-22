package com.careerforge.careerforge.dto;

import jakarta.validation.constraints.NotBlank;

public class AIChatRequestDto {

    @NotBlank(message = "Prompt cannot be empty")
    private String prompt;

    private String category = "GENERAL";

    public AIChatRequestDto() {
    }

    public AIChatRequestDto(String prompt, String category) {
        this.prompt = prompt;
        this.category = category;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
