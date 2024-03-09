package com.site.admin.services.category_interface;

import com.site.admin.models.entities.Category;
import org.springframework.stereotype.Service;

/**
 * Интерфейс CategoryService предоставляет методы для сохранения, обновления и удаления категорий.
 */
@Service
public interface CategoryService {

    /**
     * Сохраняет категорию в базе данных. Если категория новая, то она будет создана, иначе - обновлена.
     *
     * @param category объект категории, который необходимо сохранить.
     * @return сохраненный объект категории с заполненным идентификатором.
     */
    Category save(Category category);

    /**
     * Обновляет существующую категорию в базе данных. Метод предполагает, что категория уже существует.
     *
     * @param category объект категории с обновленными данными.
     * @return обновленный объект категории.
     */
    Category update(Category category);

    /**
     * Удаляет категорию по ее идентификатору.
     *
     * @param id идентификатор категории, которую необходимо удалить.
     */
    void deleteById(Long id);
}
