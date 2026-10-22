package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.Certification;
import com.careerforge.careerforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {

    List<Certification> findByUserOrderByIssueDateDesc(User user);

    Optional<Certification> findByIdAndUser(Long id, User user);

    long countByUser(User user);
}
