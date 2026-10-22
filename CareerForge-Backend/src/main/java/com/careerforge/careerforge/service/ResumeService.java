package com.careerforge.careerforge.service;

import com.careerforge.careerforge.dto.ResumeRenameRequestDto;
import com.careerforge.careerforge.dto.ResumeResponseDto;
import com.careerforge.careerforge.dto.ResumeStatsDto;
import com.careerforge.careerforge.dto.ResumeVersionDto;
import com.careerforge.careerforge.entity.Resume;
import com.careerforge.careerforge.entity.ResumeVersion;
import com.careerforge.careerforge.entity.User;
import com.careerforge.careerforge.repository.ResumeRepository;
import com.careerforge.careerforge.repository.ResumeVersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final ResumeRepository resumeRepository;
    private final ResumeVersionRepository resumeVersionRepository;
    private final FileStorageService fileStorageService;

    public ResumeService(ResumeRepository resumeRepository,
                         ResumeVersionRepository resumeVersionRepository,
                         FileStorageService fileStorageService) {
        this.resumeRepository = resumeRepository;
        this.resumeVersionRepository = resumeVersionRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * Uploads a new resume and establishes Version 1.
     */
    @Transactional
    public ResumeResponseDto uploadResume(User user, String resumeName, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select a resume file to upload.");
        }

        // Store file safely
        FileStorageService.StoredFileResult stored = fileStorageService.storeFile(file);

        // Derive resume title if not explicitly provided
        String title = StringUtils.hasText(resumeName) ? resumeName.trim() : stripExtension(stored.getOriginalFileName());
        if (title.length() > 150) {
            title = title.substring(0, 150);
        }

        Resume resume = new Resume(user, title);

        // Check if user has no other resumes; if so, make this primary
        long existingCount = resumeRepository.countByUser(user);
        if (existingCount == 0) {
            resume.setPrimary(true);
        }

        ResumeVersion version1 = new ResumeVersion(
                resume,
                1,
                stored.getOriginalFileName(),
                stored.getStoredFileName(),
                stored.getFileType(),
                stored.getFileSize(),
                stored.getStoragePath()
        );

        resume.addVersion(version1);
        Resume savedResume = resumeRepository.save(resume);

        log.info("Student '{}' uploaded new resume '{}' (ID: {}) with Version 1",
                user.getEmail(), savedResume.getResumeName(), savedResume.getId());

        List<ResumeVersion> versions = List.of(version1);
        return new ResumeResponseDto(savedResume, version1, versions);
    }

    /**
     * Returns all resumes belonging to the authenticated student.
     */
    @Transactional(readOnly = true)
    public List<ResumeResponseDto> getUserResumes(User user) {
        List<Resume> resumes = resumeRepository.findByUserOrderByIsPrimaryDescUpdatedAtDesc(user);
        List<ResumeResponseDto> dtoList = new ArrayList<>();

        for (Resume resume : resumes) {
            List<ResumeVersion> versions = resumeVersionRepository.findByResumeOrderByVersionNumberDesc(resume);
            ResumeVersion latest = versions.isEmpty() ? null : versions.get(0);
            dtoList.add(new ResumeResponseDto(resume, latest, versions));
        }

        return dtoList;
    }

    /**
     * Retrieves resume metadata and version history for an authorized student.
     */
    @Transactional(readOnly = true)
    public ResumeResponseDto getResumeById(User user, Long resumeId) {
        Resume resume = resumeRepository.findByIdAndUser(resumeId, user)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found or access denied."));

        List<ResumeVersion> versions = resumeVersionRepository.findByResumeOrderByVersionNumberDesc(resume);
        ResumeVersion latest = versions.isEmpty() ? null : versions.get(0);
        return new ResumeResponseDto(resume, latest, versions);
    }

    /**
     * Renames a resume display name.
     */
    @Transactional
    public ResumeResponseDto renameResume(User user, Long resumeId, ResumeRenameRequestDto dto) {
        Resume resume = resumeRepository.findByIdAndUser(resumeId, user)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found or access denied."));

        if (!StringUtils.hasText(dto.getResumeName())) {
            throw new IllegalArgumentException("Resume name cannot be empty.");
        }

        resume.setResumeName(dto.getResumeName().trim());
        resume.setUpdatedAt(LocalDateTime.now());
        Resume saved = resumeRepository.save(resume);

        List<ResumeVersion> versions = resumeVersionRepository.findByResumeOrderByVersionNumberDesc(saved);
        ResumeVersion latest = versions.isEmpty() ? null : versions.get(0);
        return new ResumeResponseDto(saved, latest, versions);
    }

    /**
     * Uploads a new version for an existing resume while preserving version history.
     */
    @Transactional
    public ResumeResponseDto uploadNewVersion(User user, Long resumeId, MultipartFile file) {
        Resume resume = resumeRepository.findByIdAndUser(resumeId, user)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found or access denied."));

        FileStorageService.StoredFileResult stored = fileStorageService.storeFile(file);

        int nextVersionNumber = resume.getCurrentVersion() + 1;

        ResumeVersion newVersion = new ResumeVersion(
                resume,
                nextVersionNumber,
                stored.getOriginalFileName(),
                stored.getStoredFileName(),
                stored.getFileType(),
                stored.getFileSize(),
                stored.getStoragePath()
        );

        resume.addVersion(newVersion);
        Resume saved = resumeRepository.save(resume);

        log.info("Student '{}' added Version {} to resume '{}' (ID: {})",
                user.getEmail(), nextVersionNumber, saved.getResumeName(), saved.getId());

        List<ResumeVersion> versions = resumeVersionRepository.findByResumeOrderByVersionNumberDesc(saved);
        return new ResumeResponseDto(saved, newVersion, versions);
    }

    /**
     * Returns full version history for a specific resume.
     */
    @Transactional(readOnly = true)
    public List<ResumeVersionDto> getResumeVersions(User user, Long resumeId) {
        Resume resume = resumeRepository.findByIdAndUser(resumeId, user)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found or access denied."));

        return resumeVersionRepository.findByResumeOrderByVersionNumberDesc(resume)
                .stream()
                .map(ResumeVersionDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Sets a resume as primary, ensuring only one primary resume exists for the student.
     */
    @Transactional
    public ResumeResponseDto setPrimaryResume(User user, Long resumeId) {
        Resume resume = resumeRepository.findByIdAndUser(resumeId, user)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found or access denied."));

        // Reset any other primary resumes for this student
        resumeRepository.resetOtherPrimaries(user, resumeId);

        resume.setPrimary(true);
        resume.setUpdatedAt(LocalDateTime.now());
        Resume saved = resumeRepository.save(resume);

        List<ResumeVersion> versions = resumeVersionRepository.findByResumeOrderByVersionNumberDesc(saved);
        ResumeVersion latest = versions.isEmpty() ? null : versions.get(0);
        return new ResumeResponseDto(saved, latest, versions);
    }

    /**
     * Deletes a resume, all its versions, and its physical files.
     */
    @Transactional
    public void deleteResume(User user, Long resumeId) {
        Resume resume = resumeRepository.findByIdAndUser(resumeId, user)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found or access denied."));

        boolean wasPrimary = resume.isPrimary();

        List<ResumeVersion> versions = resumeVersionRepository.findByResumeOrderByVersionNumberDesc(resume);

        // Delete all physical files
        for (ResumeVersion version : versions) {
            fileStorageService.deletePhysicalFile(version.getStoredFileName());
        }

        resumeRepository.delete(resume);
        log.info("Student '{}' deleted resume '{}' (ID: {}) and {} physical version file(s)",
                user.getEmail(), resume.getResumeName(), resumeId, versions.size());

        // If deleted resume was primary, designate another one as primary if available
        if (wasPrimary) {
            List<Resume> remaining = resumeRepository.findByUserOrderByIsPrimaryDescUpdatedAtDesc(user);
            if (!remaining.isEmpty()) {
                Resume newPrimary = remaining.get(0);
                newPrimary.setPrimary(true);
                resumeRepository.save(newPrimary);
            }
        }
    }

    /**
     * Loads a resume file resource for download.
     */
    @Transactional(readOnly = true)
    public DownloadResult downloadResumeFile(User user, Long resumeId, Integer versionNumber) {
        Resume resume = resumeRepository.findByIdAndUser(resumeId, user)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found or access denied."));

        ResumeVersion version;
        if (versionNumber != null && versionNumber > 0) {
            version = resumeVersionRepository.findByResumeAndVersionNumber(resume, versionNumber)
                    .orElseThrow(() -> new IllegalArgumentException("Version " + versionNumber + " not found."));
        } else {
            version = resumeVersionRepository.findFirstByResumeOrderByVersionNumberDesc(resume)
                    .orElseThrow(() -> new IllegalArgumentException("No versions available for this resume."));
        }

        Resource resource = fileStorageService.loadFileAsResource(version.getStoredFileName());
        return new DownloadResult(resource, version.getOriginalFileName(), version.getFileType(), version.getFileSize());
    }

    /**
     * Aggregates resume statistics for dashboard and summary widgets.
     */
    @Transactional(readOnly = true)
    public ResumeStatsDto getResumeStats(User user) {
        long totalResumes = resumeRepository.countByUser(user);
        Optional<Resume> primaryOpt = resumeRepository.findByUserAndIsPrimaryTrue(user);
        long totalVersions = resumeVersionRepository.countTotalVersionsByUserId(user.getId());

        boolean hasPrimary = primaryOpt.isPresent();
        String primaryName = hasPrimary ? primaryOpt.get().getResumeName() : null;
        Long primaryId = hasPrimary ? primaryOpt.get().getId() : null;

        List<Resume> list = resumeRepository.findByUserOrderByIsPrimaryDescUpdatedAtDesc(user);
        LocalDateTime latestDate = list.isEmpty() ? null : list.get(0).getUpdatedAt();

        return new ResumeStatsDto(totalResumes, hasPrimary, primaryName, primaryId, latestDate, totalVersions);
    }

    private String stripExtension(String filename) {
        if (filename == null) return "Resume";
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0) {
            return filename.substring(0, lastDot);
        }
        return filename;
    }

    public static class DownloadResult {
        private final Resource resource;
        private final String originalFileName;
        private final String fileType;
        private final long fileSize;

        public DownloadResult(Resource resource, String originalFileName, String fileType, long fileSize) {
            this.resource = resource;
            this.originalFileName = originalFileName;
            this.fileType = fileType;
            this.fileSize = fileSize;
        }

        public Resource getResource() { return resource; }
        public String getOriginalFileName() { return originalFileName; }
        public String getFileType() { return fileType; }
        public long getFileSize() { return fileSize; }
    }
}
