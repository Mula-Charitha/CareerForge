package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DsaProblemRepository extends JpaRepository<DsaProblem, Long> {

    Optional<DsaProblem> findByIdAndUser(Long id, User user);

    List<DsaProblem> findByUser(User user);

    @Query("SELECT p FROM DsaProblem p WHERE p.user = :user " +
           "AND (:topic IS NULL OR :topic = '' OR LOWER(p.topic) = LOWER(:topic)) " +
           "AND (:difficulty IS NULL OR p.difficulty = :difficulty) " +
           "AND (:status IS NULL OR p.status = :status) " +
           "AND (:platform IS NULL OR p.platform = :platform) " +
           "AND (:search IS NULL OR :search = '' OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "ORDER BY p.updatedAt DESC")
    List<DsaProblem> filterProblems(
            @Param("user") User user,
            @Param("topic") String topic,
            @Param("difficulty") Difficulty difficulty,
            @Param("status") Status status,
            @Param("platform") Platform platform,
            @Param("search") String search
    );

    long countByUser(User user);

    long countByUserAndStatus(User user, Status status);

    long countByUserAndDifficulty(User user, Difficulty difficulty);

    long countByUserAndTopic(User user, String topic);

    long countByUserAndTopicAndStatus(User user, String topic, Status status);
}
