package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.AptitudePracticeSession;
import com.careerforge.careerforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AptitudePracticeSessionRepository extends JpaRepository<AptitudePracticeSession, Long> {

    Optional<AptitudePracticeSession> findByIdAndUser(Long id, User user);

    List<AptitudePracticeSession> findByUserOrderByStartedAtDesc(User user);

    List<AptitudePracticeSession> findByUserAndCompletedTrueOrderByCompletedAtDesc(User user);

    long countByUser(User user);

    long countByUserAndCompletedTrue(User user);
}
