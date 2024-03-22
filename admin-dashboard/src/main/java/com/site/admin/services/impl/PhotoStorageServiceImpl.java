package com.site.admin.services.impl;

import com.site.admin.exceptions.SaveFileException;
import com.site.admin.services.PhotoStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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
    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5 MB

    @Value("${admin.images-upload-path}")
    private String imagesUploadPath;

    @Value("${admin.server-url}")
    private String serverUrl;

    @Override
    public String store(MultipartFile photo) {
        Assert.notNull(photo, "Фотография не должна быть null");
        Assert.isTrue(!photo.isEmpty(), "Файл фотографии не должен быть пустым");
        Assert.isTrue(photo.getSize() <= MAX_SIZE, "Размер файла превышает допустимый предел в 5 МБ");
        Assert.isTrue(photo.getContentType().startsWith("image/"), "Недопустимый тип файла: только изображения");

        String fileName = UUID.randomUUID() + FILE_EXTENSION;
        Path filePath = Paths.get(imagesUploadPath + IMAGE_FOLDER + fileName);

        try {
            Files.createDirectories(filePath.getParent());
            photo.transferTo(filePath.toFile());
        } catch (IOException e) {
            String errorMsg = String.format("Не удалось сохранить фотографию с именем файла: %s", fileName);
            log.error(errorMsg, e);
            throw new SaveFileException(errorMsg, e);
        }

        return serverUrl + IMAGE_FOLDER + fileName;
    }
}
