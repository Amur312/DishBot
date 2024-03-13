package com.site.admin.controllers;

import com.site.admin.models.entities.Product;
import com.site.admin.services.ProductService;
import com.site.admin.services.category_interface.CategoryFinder;
import com.site.admin.utils.ControllerUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryFinder categoryFinder;

    @Autowired
    public ProductController(ProductService productService, CategoryFinder categoryFinder) {
        this.productService = productService;
        this.categoryFinder = categoryFinder;
    }

    @GetMapping
    public String showAllProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "main/products/all";
    }

    @GetMapping("/add")
    public String showAddProduct(Model model) {
        model.addAttribute("categories", categoryFinder.findAll());
        return "main/products/add";
    }

    @GetMapping("/edit/{product}")
    public String showEditProduct(Model model, @PathVariable Product product) {
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryFinder.findAll());
        return "main/products/edit";
    }

    @PostMapping("/create")
    public String createProduct(
            @Valid Product product,
            BindingResult bindingResult,
            Model model,
            @RequestParam MultipartFile photo
    ) {
        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.findErrors(bindingResult));
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryFinder.findAll());
            return "main/products/add";
        }
        productService.save(product, photo);
        return "redirect:/products";
    }

    @PostMapping("/update")
    public String updateProduct(
            @Valid Product product,
            BindingResult bindingResult,
            Model model,
            @RequestParam(required = false) MultipartFile photo
    ) {
        if (bindingResult.hasErrors()) {
            model.mergeAttributes(ControllerUtils.findErrors(bindingResult));
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryFinder.findAll());
            return "main/products/edit";
        }

        productService.update(product, photo);
        return "redirect:/products/edit/" + product.getId();
    }

    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Integer id) {
        productService.deleteById(id);
        return "redirect:/products";
    }

}
