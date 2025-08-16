package com.busify.project.common.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Upload file to Cloudinary
     * 
     * @param file   - The file to upload
     * @param folder - The folder to upload to (e.g., "licenses", "avatars")
     * @return The secure URL of the uploaded file
     * @throws IOException if upload fails
     */
    public String uploadFile(MultipartFile file, String folder) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Validate file type (only images and PDFs for licenses)
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.startsWith("busify/license") && !contentType.equals("application/pdf")
                        && !contentType.equals("image/jpeg") && !contentType.equals("image/png")
                        && !contentType.equals("image/gif"))) {
            throw new IllegalArgumentException("Only images and PDF files are allowed");
        }

        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "auto", // auto-detect file type
                "public_id", generatePublicId(file.getOriginalFilename()));

        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
        return (String) uploadResult.get("secure_url");
    }

    /**
     * Delete file from Cloudinary
     * 
     * @param publicId - The public ID of the file to delete
     * @throws IOException if deletion fails
     */
    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    /**
     * Extract public ID from Cloudinary URL
     * 
     * @param cloudinaryUrl - The full Cloudinary URL
     * @return The public ID
     */
    public String extractPublicId(String cloudinaryUrl) {
        if (cloudinaryUrl == null || !cloudinaryUrl.contains("cloudinary.com")) {
            return null;
        }

        // Extract public ID from URL like:
        // https://res.cloudinary.com/xxx/image/upload/v123/folder/filename.ext
        String[] parts = cloudinaryUrl.split("/");
        if (parts.length >= 2) {
            String lastPart = parts[parts.length - 1];
            // Remove file extension
            return lastPart.substring(0, lastPart.lastIndexOf('.'));
        }
        return null;
    }

    private String generatePublicId(String originalFilename) {
        if (originalFilename == null) {
            return "unnamed_" + System.currentTimeMillis();
        }

        // Remove extension and replace spaces with underscores
        String nameWithoutExt = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
        return nameWithoutExt.replaceAll("[^a-zA-Z0-9_-]", "_") + "_" + System.currentTimeMillis();
    }
}
