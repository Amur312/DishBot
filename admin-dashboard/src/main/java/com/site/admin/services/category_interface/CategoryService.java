package com.site.admin.services.category_interface;

import com.site.admin.models.entities.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {

    Category save(Category category);
    Category update(Category category);
    void deleteById(Long id);
}
