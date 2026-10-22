package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.JobApplication;
import com.careerforge.careerforge.entity.JobApplicationStatus;
import com.careerforge.careerforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUserOrderByApplicationDateDesc(User user);

    List<JobApplication> findByUserAndStatusOrderByApplicationDateDesc(User user, JobApplicationStatus status);

    Optional<JobApplication> findByIdAndUser(Long id, User user);

    long countByUser(User user);

    long countByUserAndStatus(User user, JobApplicationStatus status);
}
