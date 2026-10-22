package com.careerforge.careerforge.dto;

import java.util.List;

public class ProfileCompletionDto {

    private int completionPercentage;
    private int completedFieldsCount;
    private int totalFieldsCount;
    private List<String> missingFields;

    public ProfileCompletionDto() {
    }

    public ProfileCompletionDto(int completionPercentage, int completedFieldsCount, int totalFieldsCount, List<String> missingFields) {
        this.completionPercentage = completionPercentage;
        this.completedFieldsCount = completedFieldsCount;
        this.totalFieldsCount = totalFieldsCount;
        this.missingFields = missingFields;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public int getCompletedFieldsCount() {
        return completedFieldsCount;
    }

    public void setCompletedFieldsCount(int completedFieldsCount) {
        this.completedFieldsCount = completedFieldsCount;
    }

    public int getTotalFieldsCount() {
        return totalFieldsCount;
    }

    public void setTotalFieldsCount(int totalFieldsCount) {
        this.totalFieldsCount = totalFieldsCount;
    }

    public List<String> getMissingFields() {
        return missingFields;
    }

    public void setMissingFields(List<String> missingFields) {
        this.missingFields = missingFields;
    }
}
