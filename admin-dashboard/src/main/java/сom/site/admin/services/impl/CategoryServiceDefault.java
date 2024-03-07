package сom.site.admin.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import сom.site.admin.exceptions.ValidationException;
import сom.site.admin.models.entities.Category;
import сom.site.admin.repositories.CategoryRepository;
import сom.site.admin.services.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceDefault implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceDefault(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }


    @Override
    public Category save(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category should not be NULL");
        }
        if (category.getId() != null) {
            throw new ValidationException("Id of Category should be NULL");
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category should not be NULL");
        }
        if (category.getId() == null) {
            throw new ValidationException("Id of Category should not be NULL");
        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of Category should not be NULL");
        }

        categoryRepository.deleteById(id);
    }
    public Category createRootCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    public Category createChildCategory(String name, Long parentId) {
        Category parentCategory = categoryRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
        Category category = new Category();
        category.setName(name);
        category.setParentCategory(parentCategory);
        return categoryRepository.save(category);
    }

    public void deleteCategoryAndChildren(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        deleteRecursively(category);
    }

    private void deleteRecursively(Category category) {
        List<Category> children = categoryRepository.findByParentCategory(category);
        for (Category child : children) {
            deleteRecursively(child);
        }
        categoryRepository.delete(category);
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

    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }
}
