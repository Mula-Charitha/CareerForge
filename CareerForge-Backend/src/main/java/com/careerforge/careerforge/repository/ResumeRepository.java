package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.Resume;
import com.careerforge.careerforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    List<Resume> findByUserOrderByIsPrimaryDescUpdatedAtDesc(User user);

    Optional<Resume> findByIdAndUser(Long id, User user);

    Optional<Resume> findByUserAndIsPrimaryTrue(User user);

    long countByUser(User user);

    @Modifying
    @Query("UPDATE Resume r SET r.isPrimary = false WHERE r.user = :user AND r.id <> :excludeId")
    void resetOtherPrimaries(@Param("user") User user, @Param("excludeId") Long excludeId);

    @Modifying
    @Query("UPDATE Resume r SET r.isPrimary = false WHERE r.user = :user")
    void resetAllPrimaries(@Param("user") User user);
}
