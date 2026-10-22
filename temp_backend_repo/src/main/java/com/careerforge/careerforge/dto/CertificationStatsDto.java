package com.careerforge.careerforge.dto;

public class CertificationStatsDto {

    private long totalCertifications;

    public CertificationStatsDto() {
    }

    public CertificationStatsDto(long totalCertifications) {
        this.totalCertifications = totalCertifications;
    }

    public long getTotalCertifications() {
        return totalCertifications;
    }

    public void setTotalCertifications(long totalCertifications) {
        this.totalCertifications = totalCertifications;
    }
}
