package com.site.admin.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.site.admin.models.entities.Category;
import com.site.admin.services.category_interface.CategoryFinder;
import com.site.admin.services.category_interface.CategoryHierarchyManagement;
import com.site.admin.services.category_interface.CategoryService;
import com.site.admin.utils.ControllerUtils;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryFinder categoryFinder;
    private final CategoryHierarchyManagement categoryHierarchyManagement;
    @Autowired
    public CategoryController(CategoryService categoryService, CategoryFinder categoryFinder, CategoryHierarchyManagement categoryHierarchyManagement) {
        this.categoryService = categoryService;
        this.categoryFinder = categoryFinder;
        this.categoryHierarchyManagement = categoryHierarchyManagement;
    }

    @GetMapping
    public String showAllCategories(Model model) {
        model.addAttribute("categories", categoryFinder.findAll());
        return "main/categories/all";
    }

    @GetMapping("/add")
    public String showAddCategory(Model model) {
        List<Category> parentCategories = categoryFinder.findAllRootCategories();
        model.addAttribute("parentCategories", parentCategories);
        return "main/categories/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditCategory(@PathVariable Long id, Model model) {
        Category category = categoryFinder.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        model.addAttribute("parentCategories", categoryFinder.findAllRootCategories());
        return "main/categories/edit";
    }

    @PostMapping("/create")
    public String createCategory(@RequestParam("parent_id") Optional<Long> parentId, @Valid Category category, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.findErrors(bindingResult));
            model.addAttribute("category", category);
            List<Category> parentCategories = categoryFinder.findAllRootCategories();
            model.addAttribute("parentCategories", parentCategories);
            return "main/categories/add";
        }

        parentId.ifPresent(aLong -> {
            Category parent = categoryFinder.findById(aLong)
                    .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
            category.setParentCategory(parent);
        });

        categoryService.save(category);
        return "redirect:/categories";
    }


    @PostMapping("/update")
    public String updateCategory(@Valid Category category, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.findErrors(bindingResult));
            model.addAttribute("category", category);
            return "main/categories/edit";
        }

        categoryService.update(category);
        return "redirect:/categories";
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Long id) {
        categoryHierarchyManagement.deleteCategoryAndChildren(id);
        return "redirect:/categories";
    }
}