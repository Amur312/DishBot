package com.site.admin.services.impl;

import com.site.admin.exceptions.SaveFileException;
import com.site.admin.services.PhotoStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class PhotoStorageServiceImpl implements PhotoStorageService {
    private static final String IMAGE_FOLDER = "/images/";
    private static final String FILE_EXTENSION = ".jpg";
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    @Value("${admin.images-upload-path}")
    private String imagesUploadPath;

    @Value("${admin.server-url}")
    private String serverUrl;

    @Override
    public String store(MultipartFile photo) {
        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("Фотография не должна быть пустой");
        }

        if (photo.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("Размер файла превышает допустимый предел");
        }

        if (!photo.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Недопустимый тип файла");
        }

        String fileName = UUID.randomUUID() + FILE_EXTENSION;
        Path filePath = Paths.get(imagesUploadPath + IMAGE_FOLDER + fileName);

        try {
            Files.createDirectories(filePath.getParent());
            photo.transferTo(filePath.toFile());
        } catch (IOException e) {
            log.error("Не удалось сохранить фотографию", e);
            throw new SaveFileException("Не удалось сохранить фотографию", e);
        }

        return serverUrl + IMAGE_FOLDER + fileName;
    }

}
