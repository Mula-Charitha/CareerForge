package com.careerforge.careerforge.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageService.class);

    private static final long MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024; // 5 MB

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "docx");

    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "application/pdf",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/msword",
            "application/octet-stream" // Some browsers send octet-stream for docx
    );

    private final Path rootStorageLocation;

    public FileStorageService(@Value("${careerforge.resume.storage-dir:uploads/resumes}") String storageDir) {
        this.rootStorageLocation = Paths.get(storageDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootStorageLocation);
            log.info("Initialized resume file storage directory at: {}", this.rootStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize file storage directory: " + this.rootStorageLocation, e);
        }
    }

    /**
     * Validates and saves an uploaded resume file securely.
     */
    public StoredFileResult storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file.");
        }

        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new IllegalArgumentException("File size exceeds the 5 MB limit. Selected file size: "
                    + (file.getSize() / (1024 * 1024)) + " MB.");
        }

        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "resume"
        );

        // Security: Prevent path traversal in original file name
        if (originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
            originalFilename = Paths.get(originalFilename).getFileName().toString();
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Unsupported file type '." + extension +
                    "'. Only PDF and DOCX files are supported.");
        }

        String contentType = file.getContentType();
        if (contentType != null && !isValidContentType(contentType)) {
            throw new IllegalArgumentException("Invalid file content type: " + contentType +
                    ". Only PDF and DOCX files are allowed.");
        }

        // Generate unique, non-colliding server-side filename
        String normalizedFileType = extension.toUpperCase();
        String storedFileName = UUID.randomUUID() + "_" + System.currentTimeMillis() + "." + extension;

        try {
            Path targetLocation = this.rootStorageLocation.resolve(storedFileName).normalize();

            // Security check: ensure target path is within rootStorageLocation
            if (!targetLocation.startsWith(this.rootStorageLocation)) {
                throw new SecurityException("Cannot store file outside current storage directory.");
            }

            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Successfully stored resume file '{}' as '{}'", originalFilename, storedFileName);

            // Relative path for database reference (never exposing absolute filesystem path)
            String storagePath = "resumes/" + storedFileName;

            return new StoredFileResult(originalFilename, storedFileName, normalizedFileType, file.getSize(), storagePath);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file " + originalFilename + ". Please try again.", ex);
        }
    }

    /**
     * Loads a file resource for download given the stored filename.
     */
    public Resource loadFileAsResource(String storedFileName) {
        try {
            // Security: Prevent path traversal
            if (storedFileName == null || storedFileName.contains("..") || storedFileName.contains("/") || storedFileName.contains("\\")) {
                storedFileName = Paths.get(storedFileName).getFileName().toString();
            }

            Path filePath = this.rootStorageLocation.resolve(storedFileName).normalize();

            if (!filePath.startsWith(this.rootStorageLocation)) {
                throw new SecurityException("Access to path outside storage directory is denied.");
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or cannot be read: " + storedFileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found: " + storedFileName, ex);
        }
    }

    /**
     * Safely deletes a stored file from disk.
     */
    public boolean deletePhysicalFile(String storedFileName) {
        if (storedFileName == null || storedFileName.trim().isEmpty()) {
            return false;
        }

        try {
            String safeFileName = Paths.get(storedFileName).getFileName().toString();
            Path filePath = this.rootStorageLocation.resolve(safeFileName).normalize();

            if (filePath.startsWith(this.rootStorageLocation)) {
                return Files.deleteIfExists(filePath);
            }
        } catch (Exception e) {
            log.warn("Could not delete physical file: {}", storedFileName, e);
        }
        return false;
    }

    private String getFileExtension(String filename) {
        int lastIndex = filename.lastIndexOf('.');
        if (lastIndex == -1 || lastIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(lastIndex + 1);
    }

    private boolean isValidContentType(String contentType) {
        String lower = contentType.toLowerCase();
        for (String allowed : ALLOWED_MIME_TYPES) {
            if (lower.startsWith(allowed)) {
                return true;
            }
        }
        return false;
    }

    public static class StoredFileResult {
        private final String originalFileName;
        private final String storedFileName;
        private final String fileType;
        private final long fileSize;
        private final String storagePath;

        public StoredFileResult(String originalFileName, String storedFileName, String fileType, long fileSize, String storagePath) {
            this.originalFileName = originalFileName;
            this.storedFileName = storedFileName;
            this.fileType = fileType;
            this.fileSize = fileSize;
            this.storagePath = storagePath;
        }

        public String getOriginalFileName() { return originalFileName; }
        public String getStoredFileName() { return storedFileName; }
        public String getFileType() { return fileType; }
        public long getFileSize() { return fileSize; }
        public String getStoragePath() { return storagePath; }
    }
}
