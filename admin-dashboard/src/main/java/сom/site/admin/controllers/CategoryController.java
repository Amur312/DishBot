package сom.site.admin.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import сom.site.admin.models.entities.Category;
import сom.site.admin.services.CategoryService;
import сom.site.admin.utils.ControllerUtils;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showAllCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "main/categories/all";
    }

    @GetMapping("/add")
    public String showAddCategory(Model model) {
        List<Category> parentCategories = categoryService.findAllRootCategories();
        model.addAttribute("parentCategories", parentCategories);
        return "main/categories/add";
    }

    @GetMapping("/edit/{id}")
    public String showEditCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        model.addAttribute("parentCategories", categoryService.findAllRootCategories());
        return "main/categories/edit";
    }

    @PostMapping("/create")
    public String createCategory(@RequestParam("parent_id") Optional<Long> parentId, @Valid Category category, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.findErrors(bindingResult));
            model.addAttribute("category", category);
            List<Category> parentCategories = categoryService.findAllRootCategories();
            model.addAttribute("parentCategories", parentCategories);
            return "main/categories/add";
        }

        parentId.ifPresent(aLong -> {
            Category parent = categoryService.findById(aLong)
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
        categoryService.deleteCategoryAndChildren(id);
        return "redirect:/categories";
    }
}