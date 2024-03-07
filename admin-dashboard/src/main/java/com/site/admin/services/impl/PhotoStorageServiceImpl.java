package com.site.admin.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import com.site.admin.exceptions.SaveFileException;
import com.site.admin.services.PhotoStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PhotoStorageServiceImpl implements PhotoStorageService {

    @Value("${admin.images-upload-path}")
    private String imagesUploadPath;

    @Value("${admin.server-url}")
    private String serverUrl;

    @Override
    public String store(MultipartFile photo) {
        if (photo == null) {
            throw new IllegalArgumentException("Photo should not be NULL");
        }

        String fileName = UUID.randomUUID() + ".jpg";
        String filePath = imagesUploadPath + "/" + fileName;

        try {
            photo.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new SaveFileException("Failed save photo", e);
        }

        return serverUrl + "/images/" + fileName;
    }

}