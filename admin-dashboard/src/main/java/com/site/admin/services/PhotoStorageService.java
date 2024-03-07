package com.site.admin.services;

import org.springframework.web.multipart.MultipartFile;

public interface PhotoStorageService {

    String store(MultipartFile photo);

}
