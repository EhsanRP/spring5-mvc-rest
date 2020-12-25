package guru.springframework.services;

import guru.springframework.api.v1.mapper.CategoryMapper;
import guru.springframework.api.v1.model.CategoryDTO;
import guru.springframework.domain.Category;
import guru.springframework.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CategoryServiceImplTest {

    //ASSERTIONS SAMPLE NAMES
    private static final String CATEGORY_AUSTRALIAN = "Australian";

    //ASSERTIONS SAMPLE IDs
    private static final Long CATEGORY_ID1 = 1L;

    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository, CategoryMapper.INSTANCE);
    }

    @Test
    public void getAllCategories() throws Exception {

        //given
        List<Category> categories = Arrays.asList(new Category(), new Category(), new Category());

        when(categoryRepository.findAll()).thenReturn(categories);

        //when
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategories();

        //then
        assertEquals(3, categoryDTOS.size());

    }

    @Test
    public void getCategoryByName() throws Exception {

        //given
        Category category = new Category();
        category.setId(CATEGORY_ID1);
        category.setName(CATEGORY_AUSTRALIAN);

        when(categoryRepository.findByName(anyString())).thenReturn(category);

        //when
        CategoryDTO categoryDTO = categoryService.getCategoryByName(CATEGORY_AUSTRALIAN);

        //then
        assertEquals(CATEGORY_ID1, categoryDTO.getId());
        assertEquals(CATEGORY_AUSTRALIAN, categoryDTO.getName());

    }
}