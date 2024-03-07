package сom.site.admin.services;

import java.util.List;
import java.util.Optional;
import сom.site.admin.models.entities.Category;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(Long id);

    Category save(Category category);

    Category update(Category category);

    void deleteById(Long id);

    // Новые методы
    Category createRootCategory(String name);

    Category createChildCategory(String name, Long parentId);

    void deleteCategoryAndChildren(Long categoryId);

    List<Category> findSubcategoriesByParentId(Long parentId);

    List<Category> findAllRootCategories();

    Long findParentIdByCategoryId(Long categoryId);

    Category findByName(String name);
}
