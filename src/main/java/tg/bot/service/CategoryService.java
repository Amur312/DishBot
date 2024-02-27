package tg.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tg.bot.model.Category;
import tg.bot.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name, Category parent) {
        Category category = new Category();
        category.setName(name);
        category.setParentCategory(parent);
        return categoryRepository.save(category);
    }
    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }
}
