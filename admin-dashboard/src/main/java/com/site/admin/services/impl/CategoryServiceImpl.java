package com.site.admin.services.impl;

import com.site.admin.models.entities.Category;
import com.site.admin.repositories.CategoryRepository;
import com.site.admin.services.category_interface.CategoryFinder;
import com.site.admin.services.category_interface.CategoryHierarchyManagement;
import com.site.admin.services.category_interface.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService, CategoryFinder, CategoryHierarchyManagement {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        Assert.notNull(id, "Id of Category should not be NULL");
        return categoryRepository.findById(id);
    }

    @Override
    public Category save(Category category) {
        Assert.notNull(category, "Category should not be NULL");
        Assert.isNull(category.getId(), "Id of Category should be NULL");
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        Assert.notNull(category, "Category should not be NULL");
        Assert.notNull(category.getId(), "Id of Category should not be NULL");
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        Assert.notNull(id, "Id of Category should not be NULL");
        categoryRepository.deleteById(id);
    }

    @Override
    public Category createRootCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    @Override
    public Category createChildCategory(String name, Long parentId) {
        Category parentCategory = categoryRepository.findById(parentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
        Category category = new Category();
        category.setName(name);
        category.setParentCategory(parentCategory);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteCategoryAndChildren(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        deleteRecursively(category);
    }

    private void deleteRecursively(Category category) {
        List<Category> children = categoryRepository.findByParentCategory(category);
        children.forEach(this::deleteRecursively);
        categoryRepository.delete(category);
    }

    @Override
    public List<Category> findSubcategoriesByParentId(Long parentId) {
        Assert.notNull(parentId, "Parent Id should not be NULL");
        return categoryRepository.findByParentCategory_Id(parentId);
    }

    @Override
    public List<Category> findAllRootCategories() {
        return categoryRepository.findByParentCategoryIsNull();
    }


    public Long findParentIdByCategoryId(Long categoryId) {
        Assert.notNull(categoryId, "Category Id should not be NULL");
        return categoryRepository.findById(categoryId).map(category ->
                        category.getParentCategory() != null ? category.getParentCategory().getId() : 0)
                .orElseThrow(() -> new EntityNotFoundException("Category with ID " + categoryId + " not found"));
    }


    public Category findByName(String name) {
        Assert.notNull(name, "Category name should not be NULL");
        return categoryRepository.findByName(name).orElse(null);
    }
}
