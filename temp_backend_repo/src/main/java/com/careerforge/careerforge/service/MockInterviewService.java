package com.careerforge.careerforge.service;

import com.careerforge.careerforge.dto.MockInterviewRequestDto;
import com.careerforge.careerforge.dto.MockInterviewResponseDto;
import com.careerforge.careerforge.dto.MockInterviewStatsDto;
import com.careerforge.careerforge.entity.MockInterview;
import com.careerforge.careerforge.entity.MockInterviewStatus;
import com.careerforge.careerforge.entity.MockInterviewType;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.MockInterviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MockInterviewService {

    private static final Logger log = LoggerFactory.getLogger(MockInterviewService.class);

    private final MockInterviewRepository mockInterviewRepository;

    public MockInterviewService(MockInterviewRepository mockInterviewRepository) {
        this.mockInterviewRepository = mockInterviewRepository;
    }

    @Transactional(readOnly = true)
    public List<MockInterviewResponseDto> getUserInterviews(User user, MockInterviewStatus status) {
        List<MockInterview> list;
        if (status != null) {
            list = mockInterviewRepository.findByUserAndStatusOrderByScheduledDateDesc(user, status);
        } else {
            list = mockInterviewRepository.findByUserOrderByScheduledDateDesc(user);
        }
        return list.stream().map(MockInterviewResponseDto::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MockInterviewResponseDto getInterviewById(User user, Long id) {
        MockInterview item = mockInterviewRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Mock interview session not found or access denied."));
        return new MockInterviewResponseDto(item);
    }

    @Transactional
    public MockInterviewResponseDto createInterview(User user, MockInterviewRequestDto dto) {
        MockInterview interview = new MockInterview(user, dto.getTitle().trim(), dto.getInterviewType(), dto.getScheduledDate());
        interview.setTargetCompany(dto.getTargetCompany());
        interview.setTargetRole(dto.getTargetRole());
        interview.setDurationMinutes(dto.getDurationMinutes() != null ? dto.getDurationMinutes() : 45);
        if (dto.getStatus() != null) {
            interview.setStatus(dto.getStatus());
        }
        interview.setRating(dto.getRating());
        interview.setFeedback(dto.getFeedback());
        interview.setStrengths(dto.getStrengths());
        interview.setAreasOfImprovement(dto.getAreasOfImprovement());
        interview.setNotes(dto.getNotes());

        MockInterview saved = mockInterviewRepository.save(interview);
        log.info("Student '{}' scheduled/logged mock interview '{}' (ID: {})", user.getEmail(), saved.getTitle(), saved.getId());
        return new MockInterviewResponseDto(saved);
    }

    @Transactional
    public MockInterviewResponseDto updateInterview(User user, Long id, MockInterviewRequestDto dto) {
        MockInterview interview = mockInterviewRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Mock interview session not found or access denied."));

        interview.setTitle(dto.getTitle().trim());
        if (dto.getInterviewType() != null) {
            interview.setInterviewType(dto.getInterviewType());
        }
        interview.setTargetCompany(dto.getTargetCompany());
        interview.setTargetRole(dto.getTargetRole());
        interview.setScheduledDate(dto.getScheduledDate());
        if (dto.getDurationMinutes() != null) {
            interview.setDurationMinutes(dto.getDurationMinutes());
        }
        if (dto.getStatus() != null) {
            interview.setStatus(dto.getStatus());
        }
        interview.setRating(dto.getRating());
        interview.setFeedback(dto.getFeedback());
        interview.setStrengths(dto.getStrengths());
        interview.setAreasOfImprovement(dto.getAreasOfImprovement());
        interview.setNotes(dto.getNotes());

        MockInterview saved = mockInterviewRepository.save(interview);
        return new MockInterviewResponseDto(saved);
    }

    @Transactional
    public void deleteInterview(User user, Long id) {
        MockInterview interview = mockInterviewRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Mock interview session not found or access denied."));
        mockInterviewRepository.delete(interview);
        log.info("Student '{}' deleted mock interview ID: {}", user.getEmail(), id);
    }

    @Transactional(readOnly = true)
    public MockInterviewStatsDto getInterviewStats(User user) {
        long total = mockInterviewRepository.countByUser(user);
        long completed = mockInterviewRepository.countByUserAndStatus(user, MockInterviewStatus.COMPLETED);
        long scheduled = mockInterviewRepository.countByUserAndStatus(user, MockInterviewStatus.SCHEDULED);
        long cancelled = mockInterviewRepository.countByUserAndStatus(user, MockInterviewStatus.CANCELLED);

        Double avg = mockInterviewRepository.calculateAverageRatingByUser(user);
        double roundedAvg = avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;

        Map<String, Long> distribution = new LinkedHashMap<>();
        for (MockInterviewType t : MockInterviewType.values()) {
            List<MockInterview> list = mockInterviewRepository.findByUserOrderByScheduledDateDesc(user);
            long count = list.stream().filter(m -> m.getInterviewType() == t).count();
            distribution.put(t.name(), count);
        }

        return new MockInterviewStatsDto(total, completed, scheduled, cancelled, roundedAvg, distribution);
    }
}
