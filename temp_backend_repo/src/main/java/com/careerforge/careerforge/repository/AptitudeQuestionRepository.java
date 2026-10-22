package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.AptitudeCategory;
import com.careerforge.careerforge.entity.AptitudeQuestion;
import com.careerforge.careerforge.entity.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AptitudeQuestionRepository extends JpaRepository<AptitudeQuestion, Long> {

    List<AptitudeQuestion> findByCategory(AptitudeCategory category);

    boolean existsByQuestionText(String questionText);

    @Query("SELECT q FROM AptitudeQuestion q WHERE " +
           "(:category IS NULL OR q.category = :category) AND " +
           "(:topic IS NULL OR :topic = '' OR q.topic = :topic) AND " +
           "(:difficulty IS NULL OR q.difficulty = :difficulty)")
    List<AptitudeQuestion> findFilteredQuestions(
            @Param("category") AptitudeCategory category,
            @Param("topic") String topic,
            @Param("difficulty") Difficulty difficulty
    );

    long countByCategory(AptitudeCategory category);

    long countByCategoryAndTopic(AptitudeCategory category, String topic);
}
