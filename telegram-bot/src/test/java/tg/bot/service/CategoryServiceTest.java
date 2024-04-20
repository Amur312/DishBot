package tg.bot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tg.bot.model.Category;
import tg.bot.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindSubcategoriesByParentId() {
        Category category = new Category();
        Long parentId = 1L;
        category.setId(parentId);

        Category subCategory1 = new Category();
        subCategory1.setId(12L);

        Category subCategory2 = new Category();
        subCategory2.setId(13L);

        List<Category> expectedSubCategories = Arrays.asList(subCategory1, subCategory2);
        System.out.println(expectedSubCategories);

        when(categoryRepository.findByParentCategory_Id(parentId)).thenReturn(expectedSubCategories);

        List<Category> actualSubcategories = categoryService.findSubcategoriesByParentId(parentId);

        assertEquals(expectedSubCategories, actualSubcategories);

        verify(categoryRepository, times(1)).findByParentCategory_Id(parentId);
    }

    @Test
    public void testFindAllRootCategories() {
        Category rootCategory1 = new Category();
        rootCategory1.setId(1L);
        rootCategory1.setParentCategory(null);

        Category rootCategory2 = new Category();
        rootCategory2.setId(2L);
        rootCategory2.setParentCategory(null);

        List<Category> expectedRootCategories = new ArrayList<>();
        expectedRootCategories.add(rootCategory1);
        expectedRootCategories.add(rootCategory2);

        when(categoryRepository.findByParentCategoryIsNull()).thenReturn(expectedRootCategories);

        List<Category> actualRootCategories = categoryService.findAllRootCategories();

        assertEquals(expectedRootCategories.size(), actualRootCategories.size());
        assertTrue(actualRootCategories.containsAll(expectedRootCategories));

        verify(categoryRepository, times(1)).findByParentCategoryIsNull();
    }

    @Test
    public void testFindParentIdByCategoryId_CategoryExists() {
        long categoryId = 1L;
        long parentId = 2L;

        Category category = new Category();
        category.setId(categoryId);
        Category parentCategory = new Category();
        parentCategory.setId(parentId);
        category.setParentCategory(parentCategory);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        long actualParentId = categoryService.findParentIdByCategoryId(categoryId);

        assertEquals(parentId, actualParentId);
        verify(categoryRepository, times(1)).findById(categoryId);
    }
}
