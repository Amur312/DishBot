package сom.site.admin.services;

import java.util.List;

import сom.site.admin.models.entities.Category;

public interface CategoryService {

    Category findById(Integer id);

    List<Category> findAll();

    Category save(Category category);

    Category update(Category category);

    void deleteById(Integer id);

}
