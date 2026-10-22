package com.careerforge.careerforge.repository;

import com.careerforge.careerforge.entity.StudentProfile;
import com.careerforge.careerforge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {

    Optional<StudentProfile> findByUser(User user);

    Optional<StudentProfile> findByUserEmail(String email);
}
