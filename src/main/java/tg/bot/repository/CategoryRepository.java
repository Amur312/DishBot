package tg.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tg.bot.model.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    List<Category> findByParentCategory(Category parentCategory);
    List<Category> findByParentCategoryIsNull();
    List<Category> findByParentCategory_Id(Long parentId);
    Optional<Category> findById(Long id);
}
