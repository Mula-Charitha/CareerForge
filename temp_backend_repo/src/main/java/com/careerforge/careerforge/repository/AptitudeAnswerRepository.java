package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AptitudeAnswerRepository extends JpaRepository<AptitudeAnswer, Long> {

    Optional<AptitudeAnswer> findBySessionAndQuestion(AptitudePracticeSession session, AptitudeQuestion question);

    List<AptitudeAnswer> findBySession(AptitudePracticeSession session);

    boolean existsBySessionAndQuestion(AptitudePracticeSession session, AptitudeQuestion question);

    long countBySession_User(User user);

    long countBySession_UserAndIsCorrectTrue(User user);

    long countBySession_UserAndIsCorrectFalse(User user);

    long countBySession_UserAndQuestion_Category(User user, AptitudeCategory category);

    long countBySession_UserAndQuestion_CategoryAndIsCorrectTrue(User user, AptitudeCategory category);

    long countBySession_UserAndQuestion_Topic(User user, String topic);

    long countBySession_UserAndQuestion_TopicAndIsCorrectTrue(User user, String topic);

    long countBySession_UserAndQuestion_Difficulty(User user, Difficulty difficulty);

    long countBySession_UserAndQuestion_DifficultyAndIsCorrectTrue(User user, Difficulty difficulty);
}
