package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.MockInterview;
import com.careerforge.careerforge.entity.MockInterviewStatus;
import com.careerforge.careerforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MockInterviewRepository extends JpaRepository<MockInterview, Long> {

    List<MockInterview> findByUserOrderByScheduledDateDesc(User user);

    List<MockInterview> findByUserAndStatusOrderByScheduledDateDesc(User user, MockInterviewStatus status);

    Optional<MockInterview> findByIdAndUser(Long id, User user);

    long countByUser(User user);

    long countByUserAndStatus(User user, MockInterviewStatus status);

    @Query("SELECT AVG(m.rating) FROM MockInterview m WHERE m.user = :user AND m.status = 'COMPLETED' AND m.rating IS NOT NULL")
    Double calculateAverageRatingByUser(@Param("user") User user);
}
