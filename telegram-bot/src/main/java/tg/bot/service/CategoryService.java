package tg.bot.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.bot.model.Category;
import tg.bot.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findSubcategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentCategory_Id(parentId);
    }

    public List<Category> findAllRootCategories() {
        return categoryRepository.findByParentCategoryIsNull();
    }

    public Long findParentIdByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId).map(category -> {
            return category.getParentCategory() != null ? category.getParentCategory().getId() : 0;
        }).orElseThrow(() -> new EntityNotFoundException("Category with ID " + categoryId + " not found"));
    }

}
