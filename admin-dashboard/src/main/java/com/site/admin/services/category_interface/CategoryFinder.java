package com.site.admin.services.category_interface;

import com.site.admin.models.entities.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Интерфейс CategoryFinder определяет операции поиска категорий.
 */
@Service
public interface CategoryFinder {

    /**
     * Возвращает список всех категорий.
     *
     * @return список всех категорий.
     */
    List<Category> findAll();

    /**
     * Находит категорию по её идентификатору.
     *
     * @param id идентификатор категории.
     * @return {@link Optional} объект категории, если категория найдена; пустой {@link Optional}, если категория не найдена.
     */
    Optional<Category> findById(Long id);

    /**
     * Находит подкатегории для заданного идентификатора родительской категории.
     *
     * @param parentId идентификатор родительской категории.
     * @return список подкатегорий для заданной родительской категории.
     */
    List<Category> findSubcategoriesByParentId(Long parentId);

    /**
     * Возвращает список всех корневых категорий (категорий без родителей).
     *
     * @return список всех корневых категорий.
     */
    List<Category> findAllRootCategories();
}
