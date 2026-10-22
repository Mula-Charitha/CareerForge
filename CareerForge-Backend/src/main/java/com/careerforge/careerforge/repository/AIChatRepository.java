package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.AIChatMessage;
import com.careerforge.careerforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIChatRepository extends JpaRepository<AIChatMessage, Long> {

    List<AIChatMessage> findByUserOrderByCreatedAtAsc(User user);

    List<AIChatMessage> findTop50ByUserOrderByCreatedAtAsc(User user);

    void deleteByUser(User user);
}
