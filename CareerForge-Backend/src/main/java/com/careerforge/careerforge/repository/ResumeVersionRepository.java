package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.Resume;
import com.careerforge.careerforge.entity.ResumeVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeVersionRepository extends JpaRepository<ResumeVersion, Long> {

    List<ResumeVersion> findByResumeOrderByVersionNumberDesc(Resume resume);

    Optional<ResumeVersion> findByResumeAndVersionNumber(Resume resume, int versionNumber);

    Optional<ResumeVersion> findFirstByResumeOrderByVersionNumberDesc(Resume resume);

    @Query("SELECT COUNT(v) FROM ResumeVersion v WHERE v.resume.user.id = :userId")
    long countTotalVersionsByUserId(@Param("userId") Long userId);
}
