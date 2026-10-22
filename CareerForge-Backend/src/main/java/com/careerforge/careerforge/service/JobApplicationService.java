package com.careerforge.careerforge.service;

import com.careerforge.careerforge.dto.JobApplicationRequestDto;
import com.careerforge.careerforge.dto.JobApplicationResponseDto;
import com.careerforge.careerforge.dto.JobApplicationStatsDto;
import com.careerforge.careerforge.entity.JobApplication;
import com.careerforge.careerforge.entity.JobApplicationStatus;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.JobApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobApplicationService {

    private static final Logger log = LoggerFactory.getLogger(JobApplicationService.class);

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponseDto> getUserApplications(User user, JobApplicationStatus status, String search) {
        List<JobApplication> list;
        if (status != null) {
            list = jobApplicationRepository.findByUserAndStatusOrderByApplicationDateDesc(user, status);
        } else {
            list = jobApplicationRepository.findByUserOrderByApplicationDateDesc(user);
        }

        if (search != null && !search.trim().isEmpty()) {
            String q = search.trim().toLowerCase();
            list = list.stream()
                    .filter(app -> app.getCompanyName().toLowerCase().contains(q) ||
                            app.getJobRole().toLowerCase().contains(q) ||
                            (app.getLocation() != null && app.getLocation().toLowerCase().contains(q)))
                    .collect(Collectors.toList());
        }

        return list.stream().map(JobApplicationResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public JobApplicationResponseDto getApplicationById(User user, Long id) {
        JobApplication app = jobApplicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Job application not found or access denied."));
        return new JobApplicationResponseDto(app);
    }

    @Transactional
    public JobApplicationResponseDto createApplication(User user, JobApplicationRequestDto dto) {
        JobApplication app = new JobApplication(user, dto.getCompanyName().trim(), dto.getJobRole().trim(), dto.getApplicationDate(), dto.getStatus());
        app.setJobLink(dto.getJobLink());
        app.setLocation(dto.getLocation());
        app.setJobType(dto.getJobType());
        app.setDeadline(dto.getDeadline());
        app.setInterviewDate(dto.getInterviewDate());
        app.setNotes(dto.getNotes());

        JobApplication saved = jobApplicationRepository.save(app);
        log.info("Student '{}' added application for '{}' at '{}'", user.getEmail(), saved.getJobRole(), saved.getCompanyName());
        return new JobApplicationResponseDto(saved);
    }

    @Transactional
    public JobApplicationResponseDto updateApplication(User user, Long id, JobApplicationRequestDto dto) {
        JobApplication app = jobApplicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Job application not found or access denied."));

        app.setCompanyName(dto.getCompanyName().trim());
        app.setJobRole(dto.getJobRole().trim());
        app.setJobLink(dto.getJobLink());
        app.setLocation(dto.getLocation());
        app.setJobType(dto.getJobType());
        app.setApplicationDate(dto.getApplicationDate());
        app.setDeadline(dto.getDeadline());
        if (dto.getStatus() != null) {
            app.setStatus(dto.getStatus());
        }
        app.setInterviewDate(dto.getInterviewDate());
        app.setNotes(dto.getNotes());

        JobApplication saved = jobApplicationRepository.save(app);
        return new JobApplicationResponseDto(saved);
    }

    @Transactional
    public void deleteApplication(User user, Long id) {
        JobApplication app = jobApplicationRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Job application not found or access denied."));
        jobApplicationRepository.delete(app);
        log.info("Student '{}' deleted application ID: {}", user.getEmail(), id);
    }

    @Transactional(readOnly = true)
    public JobApplicationStatsDto getApplicationStats(User user) {
        long total = jobApplicationRepository.countByUser(user);
        long applied = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.APPLIED);
        long assessment = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.ONLINE_ASSESSMENT);
        long interview = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.INTERVIEW);
        long offer = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.OFFER);
        long rejected = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.REJECTED);
        long saved = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.SAVED);
        long withdrawn = jobApplicationRepository.countByUserAndStatus(user, JobApplicationStatus.WITHDRAWN);

        Map<String, Long> distribution = new LinkedHashMap<>();
        for (JobApplicationStatus s : JobApplicationStatus.values()) {
            distribution.put(s.name(), jobApplicationRepository.countByUserAndStatus(user, s));
        }

        return new JobApplicationStatsDto(total, applied, assessment, interview, offer, rejected, saved, withdrawn, distribution);
    }
}
