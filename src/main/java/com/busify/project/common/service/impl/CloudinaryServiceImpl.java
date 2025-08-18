package com.busify.project.common.service.impl;

import com.busify.project.common.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    // Allowed file types for uploads
    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/bmp", "image/webp");

    private static final List<String> ALLOWED_DOCUMENT_TYPES = Arrays.asList(
            "application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

    // Max file size (5MB)
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    @Override
    public String uploadFile(MultipartFile file, String folder) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        // Validate file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size cannot exceed 5MB");
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || (!ALLOWED_IMAGE_TYPES.contains(contentType) &&
                !ALLOWED_DOCUMENT_TYPES.contains(contentType))) {
            throw new IllegalArgumentException("Invalid file type. Only images and PDF/DOC files are allowed");
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadParams = (Map<String, Object>) ObjectUtils.asMap(
                    "resource_type", "auto");

            // Add folder if specified
            if (folder != null && !folder.trim().isEmpty()) {
                uploadParams.put("folder", folder);
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String publicId = "file_" + System.currentTimeMillis() + fileExtension;
            uploadParams.put("public_id", publicId);

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(),
                    uploadParams);

            String uploadedUrl = (String) uploadResult.get("secure_url");
            log.info("File uploaded successfully to Cloudinary: {}", uploadedUrl);

            return uploadedUrl;

        } catch (IOException e) {
            log.error("Error uploading file to Cloudinary", e);
            throw new Exception("Failed to upload file to Cloudinary: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteFile(String publicId) throws Exception {
        if (publicId == null || publicId.trim().isEmpty()) {
            throw new IllegalArgumentException("Public ID cannot be null or empty");
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> deleteResult = (Map<String, Object>) cloudinary.uploader().destroy(publicId,
                    ObjectUtils.emptyMap());
            String result = (String) deleteResult.get("result");

            boolean success = "ok".equals(result);
            if (success) {
                log.info("File deleted successfully from Cloudinary: {}", publicId);
            } else {
                log.warn("Failed to delete file from Cloudinary: {}. Result: {}", publicId, result);
            }

            return success;

        } catch (IOException e) {
            log.error("Error deleting file from Cloudinary: {}", publicId, e);
            throw new Exception("Failed to delete file from Cloudinary: " + e.getMessage());
        }
    }

    @Override
    public String extractPublicId(String cloudinaryUrl) {
        if (cloudinaryUrl == null || cloudinaryUrl.trim().isEmpty()) {
            return null;
        }

        try {
            // Pattern to extract public ID from Cloudinary URL
            // Example URL:
            // https://res.cloudinary.com/cloud_name/image/upload/v1234567890/folder/filename.jpg
            // Public ID would be: folder/filename

            Pattern pattern = Pattern.compile(".*cloudinary\\.com/[^/]+/[^/]+/upload/(?:v\\d+/)?(.+?)(?:\\.[^.]+)?$");
            Matcher matcher = pattern.matcher(cloudinaryUrl);

            if (matcher.find()) {
                String publicId = matcher.group(1);
                log.debug("Extracted public ID: {} from URL: {}", publicId, cloudinaryUrl);
                return publicId;
            }

            log.warn("Could not extract public ID from URL: {}", cloudinaryUrl);
            return null;

        } catch (Exception e) {
            log.error("Error extracting public ID from URL: {}", cloudinaryUrl, e);
            return null;
        }
    }
}
