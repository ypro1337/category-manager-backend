package univ.rouen.categorymanagement.service;
import univ.rouen.categorymanagement.dto.CategoryDto;
import java.util.List;

    public interface CategoryService {
        CategoryDto createCategory(CategoryDto dto);
        CategoryDto updateCategory(Long id, CategoryDto dto);
        void deleteCategory(Long id);
        CategoryDto getCategory(Long id);
        List<CategoryDto> getAllCategories(int page, int size, String sort, String filter);
    }

