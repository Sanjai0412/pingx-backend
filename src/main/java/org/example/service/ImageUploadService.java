package org.example.service;

import java.io.InputStream;

public interface ImageUploadService {
    public String convertImageToUrl(InputStream image);
}