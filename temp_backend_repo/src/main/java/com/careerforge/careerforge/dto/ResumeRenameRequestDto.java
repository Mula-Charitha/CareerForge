package com.careerforge.careerforge.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResumeRenameRequestDto {

    @NotBlank(message = "Resume name cannot be blank")
    @Size(min = 1, max = 150, message = "Resume name must be between 1 and 150 characters")
    private String resumeName;

    public ResumeRenameRequestDto() {
    }

    public ResumeRenameRequestDto(String resumeName) {
        this.resumeName = resumeName;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }
}
