package guru.springframework.api.v1.mapper;

import guru.springframework.api.v1.model.CategoryDTO;
import guru.springframework.domain.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryMapperTest {

    //ASSERTIONS SAMPLE NAMES
    private static final String CATEGORY_AUSTRALIAN = "Australian";

    //ASSERTIONS SAMPLE IDs
    private static final Long CATEGORY_ID1 = 1L;

    CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    @Test
    public void categoryToCategoryDTO() {

        //GIVEN
        Category category = Category.builder().id(CATEGORY_ID1).name(CATEGORY_AUSTRALIAN).build();

        //WHEN
        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(category);

        //THEN
        assertEquals(CATEGORY_ID1, categoryDTO.getId());
        assertEquals(CATEGORY_AUSTRALIAN, categoryDTO.getName());

    }
}