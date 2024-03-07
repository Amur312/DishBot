package com.site.admin.services.impl;

import java.util.List;

import com.site.admin.exceptions.ValidationException;
import com.site.admin.services.PhotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.site.admin.models.entities.Product;
import com.site.admin.repositories.ProductRepository;
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
        if (id == null) {
            throw new IllegalArgumentException("Id of Product should not be NULL");
        }
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public Product save(Product product, MultipartFile photo) {
        if (product == null) {
            throw new IllegalArgumentException("Product should not be NULL");
        }
        if (product.getId() != null) {
            throw new ValidationException("Id of Product should be NULL");
        }

        product.setPhotoUrl(photoStorageService.store(photo));
        return repository.save(product);
    }

    @Override
    public Product update(Product product, MultipartFile photo) {
        if (product == null) {
            throw new IllegalArgumentException("Product should not be NULL");
        }
        if (product.getId() == null) {
            throw new ValidationException("Id of Product should not be NULL");
        }

        if (!photo.isEmpty()) {
            product.setPhotoUrl(photoStorageService.store(photo));
        }
        return repository.save(product);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of Product should not be NULL");
        }

        repository.deleteById(id);
    }

}
