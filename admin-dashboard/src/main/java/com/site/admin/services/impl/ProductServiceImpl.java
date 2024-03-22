package com.site.admin.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.site.admin.exceptions.ValidationException;
import com.site.admin.models.entities.Product;
import com.site.admin.repositories.ProductRepository;
import com.site.admin.services.PhotoStorageService;
import com.site.admin.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final PhotoStorageService photoStorageService;

    @Autowired
    public ProductServiceImpl(ProductRepository repository, PhotoStorageService photoStorageService) {
        this.repository = repository;
        this.photoStorageService = photoStorageService;
    }

    @Override
    public Product findById(Integer id) {
        Assert.notNull(id, "Идентификатор продукта не должен быть пустым.");
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Product save(Product product, MultipartFile photo) {
        Assert.notNull(product, "Продукт не должен быть пустым.");
        Assert.isNull(product.getId(), "Идентификатор продукта должен быть пустым при создании нового продукта.");

        if (!photo.isEmpty()) {
            String photoUrl = photoStorageService.store(photo);
            product.setPhotoUrl(photoUrl);
        }
        return repository.save(product);
    }

    @Override
    public Product update(Product product, MultipartFile photo) {
        Assert.notNull(product, "Продукт не должен быть пустым.");
        Assert.notNull(product.getId(), "Идентификатор продукта не должен быть пустым при обновлении продукта.");

        if (!photo.isEmpty()) {
            String photoUrl = photoStorageService.store(photo);
            product.setPhotoUrl(photoUrl);
        }
        return repository.save(product);
    }

    @Override
    public void deleteById(Integer id) {
        Assert.notNull(id, "Идентификатор продукта не должен быть пустым.");
        repository.deleteById(id);
    }
}
