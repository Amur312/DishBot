package com.site.admin.services.category_interface;

import com.site.admin.models.entities.Category;
import org.springframework.stereotype.Service;

/**
 * Интерфейс CategoryHierarchyManagement определяет операции управления иерархией категорий.
 */
@Service
public interface CategoryHierarchyManagement {

    /**
     * Создает новую корневую категорию с указанным именем.
     *
     * @param name имя создаваемой корневой категории.
     * @return созданная корневая категория.
     */
    Category createRootCategory(String name);

    /**
     * Создает новую подкатегорию с указанным именем и привязывает ее к родительской категории по идентификатору parentId.
     *
     * @param name имя создаваемой подкатегории.
     * @param parentId идентификатор родительской категории.
     * @return созданная подкатегория.
     */
    Category createChildCategory(String name, Long parentId);

    /**
     * Удаляет категорию и все ее подкатегории по идентификатору.
     *
     * @param categoryId идентификатор категории, которую необходимо удалить вместе с подкатегориями.
     */
    void deleteCategoryAndChildren(Long categoryId);
}
