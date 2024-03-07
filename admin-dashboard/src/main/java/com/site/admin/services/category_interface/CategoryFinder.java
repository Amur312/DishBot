package com.site.admin.services.category_interface;

import com.site.admin.models.entities.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface CategoryFinder {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    List<Category> findSubcategoriesByParentId(Long parentId);
    List<Category> findAllRootCategories();
}
