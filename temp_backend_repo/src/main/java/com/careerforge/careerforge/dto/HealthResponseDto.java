package com.careerforge.careerforge.dto;

public class HealthResponseDto {

    private String status;
    private String message;

    public HealthResponseDto() {
    }

    public HealthResponseDto(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
