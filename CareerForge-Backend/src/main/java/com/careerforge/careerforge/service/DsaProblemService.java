package com.careerforge.careerforge.service;

import com.careerforge.careerforge.dto.DsaProblemRequestDto;
import com.careerforge.careerforge.dto.DsaProblemResponseDto;
import com.careerforge.careerforge.dto.DsaStatisticsDto;
import com.careerforge.careerforge.dto.TopicProgressDto;
import com.careerforge.careerforge.entity.*;
import com.careerforge.careerforge.repository.DsaProblemRepository;
import com.careerforge.careerforge.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DsaProblemService {

    private final DsaProblemRepository dsaProblemRepository;
    private final UserRepository userRepository;

    public DsaProblemService(DsaProblemRepository dsaProblemRepository, UserRepository userRepository) {
        this.dsaProblemRepository = dsaProblemRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<DsaProblemResponseDto> getProblems(
            String email,
            String topic,
            Difficulty difficulty,
            Status status,
            Platform platform,
            String search
    ) {
        User user = getUserByEmail(email);
        List<DsaProblem> problems = dsaProblemRepository.filterProblems(user, topic, difficulty, status, platform, search);
        return problems.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DsaProblemResponseDto getProblemById(String email, Long id) {
        User user = getUserByEmail(email);
        DsaProblem problem = dsaProblemRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("DSA problem not found with id: " + id));
        return mapToResponseDto(problem);
    }

    @Transactional
    public DsaProblemResponseDto createProblem(String email, DsaProblemRequestDto dto) {
        User user = getUserByEmail(email);

        DsaProblem problem = new DsaProblem();
        problem.setUser(user);
        problem.setTitle(dto.getTitle().trim());
        problem.setTopic(dto.getTopic().trim());
        problem.setDifficulty(dto.getDifficulty());
        problem.setPlatform(dto.getPlatform());
        problem.setProblemUrl(dto.getProblemUrl());
        problem.setStatus(dto.getStatus());
        problem.setNotes(dto.getNotes());
        problem.setProgrammingLanguage(dto.getProgrammingLanguage());

        if (dto.getStatus() == Status.SOLVED) {
            problem.setDateSolved(dto.getDateSolved() != null ? dto.getDateSolved() : LocalDate.now());
        } else {
            problem.setDateSolved(dto.getDateSolved());
        }

        DsaProblem saved = dsaProblemRepository.save(problem);
        return mapToResponseDto(saved);
    }

    @Transactional
    public DsaProblemResponseDto updateProblem(String email, Long id, DsaProblemRequestDto dto) {
        User user = getUserByEmail(email);
        DsaProblem problem = dsaProblemRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("DSA problem not found with id: " + id));

        problem.setTitle(dto.getTitle().trim());
        problem.setTopic(dto.getTopic().trim());
        problem.setDifficulty(dto.getDifficulty());
        problem.setPlatform(dto.getPlatform());
        problem.setProblemUrl(dto.getProblemUrl());
        problem.setStatus(dto.getStatus());
        problem.setNotes(dto.getNotes());
        problem.setProgrammingLanguage(dto.getProgrammingLanguage());

        if (dto.getStatus() == Status.SOLVED) {
            problem.setDateSolved(dto.getDateSolved() != null ? dto.getDateSolved() : LocalDate.now());
        } else {
            problem.setDateSolved(dto.getDateSolved());
        }

        DsaProblem saved = dsaProblemRepository.save(problem);
        return mapToResponseDto(saved);
    }

    @Transactional
    public void deleteProblem(String email, Long id) {
        User user = getUserByEmail(email);
        DsaProblem problem = dsaProblemRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("DSA problem not found with id: " + id));
        dsaProblemRepository.delete(problem);
    }

    @Transactional(readOnly = true)
    public DsaStatisticsDto getStatistics(String email) {
        User user = getUserByEmail(email);

        long totalProblems = dsaProblemRepository.countByUser(user);
        long solvedProblems = dsaProblemRepository.countByUserAndStatus(user, Status.SOLVED);
        long attemptedProblems = dsaProblemRepository.countByUserAndStatus(user, Status.ATTEMPTED);
        long notStartedProblems = dsaProblemRepository.countByUserAndStatus(user, Status.NOT_STARTED);

        long easyProblems = dsaProblemRepository.countByUserAndDifficulty(user, Difficulty.EASY);
        long mediumProblems = dsaProblemRepository.countByUserAndDifficulty(user, Difficulty.MEDIUM);
        long hardProblems = dsaProblemRepository.countByUserAndDifficulty(user, Difficulty.HARD);

        int overallProgress = totalProblems > 0
                ? (int) Math.round(((double) solvedProblems / totalProblems) * 100)
                : 0;

        List<TopicProgressDto> topicProgressList = new ArrayList<>();
        for (String topic : DsaTopic.INITIAL_TOPICS) {
            long tTotal = dsaProblemRepository.countByUserAndTopic(user, topic);
            long tSolved = dsaProblemRepository.countByUserAndTopicAndStatus(user, topic, Status.SOLVED);
            long tAttempted = dsaProblemRepository.countByUserAndTopicAndStatus(user, topic, Status.ATTEMPTED);
            long tNotStarted = dsaProblemRepository.countByUserAndTopicAndStatus(user, topic, Status.NOT_STARTED);

            int tPercentage = tTotal > 0
                    ? (int) Math.round(((double) tSolved / tTotal) * 100)
                    : 0;

            topicProgressList.add(new TopicProgressDto(topic, tTotal, tSolved, tAttempted, tNotStarted, tPercentage));
        }

        DsaStatisticsDto stats = new DsaStatisticsDto();
        stats.setTotalProblems(totalProblems);
        stats.setSolvedProblems(solvedProblems);
        stats.setAttemptedProblems(attemptedProblems);
        stats.setNotStartedProblems(notStartedProblems);
        stats.setEasyProblems(easyProblems);
        stats.setMediumProblems(mediumProblems);
        stats.setHardProblems(hardProblems);
        stats.setOverallProgress(overallProgress);
        stats.setTopicWiseProgress(topicProgressList);
        stats.setTopics(DsaTopic.INITIAL_TOPICS);

        return stats;
    }

    @Transactional(readOnly = true)
    public List<String> getTopics() {
        return DsaTopic.INITIAL_TOPICS;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    private DsaProblemResponseDto mapToResponseDto(DsaProblem problem) {
        DsaProblemResponseDto dto = new DsaProblemResponseDto();
        dto.setId(problem.getId());
        dto.setTitle(problem.getTitle());
        dto.setTopic(problem.getTopic());
        dto.setDifficulty(problem.getDifficulty());
        dto.setPlatform(problem.getPlatform());
        dto.setProblemUrl(problem.getProblemUrl());
        dto.setStatus(problem.getStatus());
        dto.setDateSolved(problem.getDateSolved());
        dto.setNotes(problem.getNotes());
        dto.setProgrammingLanguage(problem.getProgrammingLanguage());
        dto.setCreatedAt(problem.getCreatedAt());
        dto.setUpdatedAt(problem.getUpdatedAt());
        return dto;
    }
}
