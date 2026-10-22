package com.careerforge.careerforge.service;

import com.careerforge.careerforge.dto.ProfileCompletionDto;
import com.careerforge.careerforge.dto.ProfileRequestDto;
import com.careerforge.careerforge.dto.ProfileResponseDto;
import com.careerforge.careerforge.entity.StudentProfile;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.StudentProfileRepository;
import com.careerforge.careerforge.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentProfileService {

    private final StudentProfileRepository profileRepository;
    private final UserRepository userRepository;

    public StudentProfileService(StudentProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProfileResponseDto getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        StudentProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> {
                    StudentProfile newProfile = new StudentProfile(user);
                    return profileRepository.save(newProfile);
                });

        return mapToProfileResponse(profile);
    }

    @Transactional
    public ProfileResponseDto updateProfileByEmail(String email, ProfileRequestDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        if (dto.getFullName() != null && !dto.getFullName().trim().isEmpty()) {
            user.setFullName(dto.getFullName().trim());
            userRepository.save(user);
        }

        StudentProfile profile = profileRepository.findByUser(user)
                .orElseGet(() -> new StudentProfile(user));

        profile.setPhone(dto.getPhone());
        profile.setCollege(dto.getCollege());
        profile.setBranch(dto.getBranch());
        profile.setGraduationYear(dto.getGraduationYear());
        profile.setSkills(dto.getSkills());
        profile.setGithubUrl(dto.getGithubUrl());
        profile.setLinkedinUrl(dto.getLinkedinUrl());
        profile.setProfilePhoto(dto.getProfilePhoto());

        StudentProfile savedProfile = profileRepository.save(profile);
        return mapToProfileResponse(savedProfile);
    }

    @Transactional(readOnly = true)
    public ProfileCompletionDto getProfileCompletionByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        StudentProfile profile = profileRepository.findByUser(user).orElse(null);

        return calculateCompletion(user, profile);
    }

    private ProfileResponseDto mapToProfileResponse(StudentProfile profile) {
        User user = profile.getUser();

        ProfileResponseDto dto = new ProfileResponseDto();
        dto.setId(profile.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(profile.getPhone());
        dto.setCollege(profile.getCollege());
        dto.setBranch(profile.getBranch());
        dto.setGraduationYear(profile.getGraduationYear());
        dto.setSkills(profile.getSkills());
        dto.setGithubUrl(profile.getGithubUrl());
        dto.setLinkedinUrl(profile.getLinkedinUrl());
        dto.setProfilePhoto(profile.getProfilePhoto());
        dto.setUpdatedAt(profile.getUpdatedAt());

        ProfileCompletionDto completion = calculateCompletion(user, profile);
        dto.setProfileCompletionPercentage(completion.getCompletionPercentage());

        return dto;
    }

    private ProfileCompletionDto calculateCompletion(User user, StudentProfile profile) {
        List<String> missingFields = new ArrayList<>();
        int completedCount = 0;
        int totalFields = 10;

        // 1. Full Name
        if (user.getFullName() != null && !user.getFullName().trim().isEmpty()) {
            completedCount++;
        } else {
            missingFields.add("Full Name");
        }

        // 2. Email
        if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
            completedCount++;
        } else {
            missingFields.add("Email");
        }

        if (profile != null) {
            // 3. Phone
            if (profile.getPhone() != null && !profile.getPhone().trim().isEmpty()) {
                completedCount++;
            } else {
                missingFields.add("Phone Number");
            }

            // 4. College
            if (profile.getCollege() != null && !profile.getCollege().trim().isEmpty()) {
                completedCount++;
            } else {
                missingFields.add("College Name");
            }

            // 5. Branch
            if (profile.getBranch() != null && !profile.getBranch().trim().isEmpty()) {
                completedCount++;
            } else {
                missingFields.add("Branch / Major");
            }

            // 6. Graduation Year
            if (profile.getGraduationYear() != null && profile.getGraduationYear() > 2000) {
                completedCount++;
            } else {
                missingFields.add("Graduation Year");
            }

            // 7. Skills
            if (profile.getSkills() != null && !profile.getSkills().trim().isEmpty()) {
                completedCount++;
            } else {
                missingFields.add("Key Skills");
            }

            // 8. GitHub URL
            if (profile.getGithubUrl() != null && !profile.getGithubUrl().trim().isEmpty()) {
                completedCount++;
            } else {
                missingFields.add("GitHub Profile");
            }

            // 9. LinkedIn URL
            if (profile.getLinkedinUrl() != null && !profile.getLinkedinUrl().trim().isEmpty()) {
                completedCount++;
            } else {
                missingFields.add("LinkedIn Profile");
            }

            // 10. Profile Photo
            if (profile.getProfilePhoto() != null && !profile.getProfilePhoto().trim().isEmpty()) {
                completedCount++;
            } else {
                missingFields.add("Profile Picture");
            }
        } else {
            missingFields.add("Phone Number");
            missingFields.add("College Name");
            missingFields.add("Branch / Major");
            missingFields.add("Graduation Year");
            missingFields.add("Key Skills");
            missingFields.add("GitHub Profile");
            missingFields.add("LinkedIn Profile");
            missingFields.add("Profile Picture");
        }

        int percentage = (int) Math.round(((double) completedCount / totalFields) * 100);
        return new ProfileCompletionDto(percentage, completedCount, totalFields, missingFields);
    }
}
