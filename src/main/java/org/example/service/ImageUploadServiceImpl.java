package org.example.service;

import java.io.InputStream;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import org.example.config.AppConfig;

public class ImageUploadServiceImpl implements ImageUploadService {
    private final Cloudinary cloudinary;

    // Initialize cloudinary instance
    public ImageUploadServiceImpl() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", AppConfig.CLOUDINARY_CLOUD_NAME,
                "api_key", AppConfig.CLOUDINARY_API_KEY,
                "api_secret", AppConfig.CLOUDINARY_API_SECRET,
                "secure", true));
    }

    @Override
    public String convertImageToUrl(InputStream image) {

        if (image == null) {
            throw new IllegalArgumentException("Image is required");
        }

        // Upload image to cloudinary, get secure url from the result
        try {
            byte[] imageBytes = image.readAllBytes();
            Map<?, ?> uploadedResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap());
            return uploadedResult.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}