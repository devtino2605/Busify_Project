package com.busify.project.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudinaryService {

    /**
     * Upload a file to Cloudinary
     * 
     * @param file   the file to upload
     * @param folder the folder to upload to (optional)
     * @return the URL of the uploaded file
     */
    String uploadFile(MultipartFile file, String folder) throws Exception;

    /**
     * Delete a file from Cloudinary
     * 
     * @param publicId the public ID of the file to delete
     * @return true if successful, false otherwise
     */
    boolean deleteFile(String publicId) throws Exception;

    /**
     * Extract public ID from Cloudinary URL
     * 
     * @param cloudinaryUrl the Cloudinary URL
     * @return the public ID
     */
    String extractPublicId(String cloudinaryUrl);
}