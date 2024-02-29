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

    public Category createRootCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public Category createChildCategory(String name, Long parentId) {
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
        Category category = new Category();
        category.setName(name);
        category.setParentCategory(parentCategory);
        return categoryRepository.save(category);
    }

    public void deleteCategoryAndChildren(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        deleteRecursively(category);
    }
    private void deleteRecursively(Category category) {
        List<Category> children = categoryRepository.findByParentCategory(category);
        for (Category child : children) {
            deleteRecursively(child);
        }
        categoryRepository.delete(category);
    }
    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }
}
