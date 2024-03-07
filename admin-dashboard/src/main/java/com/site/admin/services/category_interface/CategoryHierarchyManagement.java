package com.site.admin.services.category_interface;

import com.site.admin.models.entities.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryHierarchyManagement {
    Category createRootCategory(String name);
    Category createChildCategory(String name, Long parentId);
    void deleteCategoryAndChildren(Long categoryId);
}
