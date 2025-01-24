package univ.rouen.categorymanagement.service.impl;

import univ.rouen.categorymanagement.dto.CategoryDto;
import univ.rouen.categorymanagement.entity.Category;
import univ.rouen.categorymanagement.exception.ResourceNotFoundException;
import univ.rouen.categorymanagement.repository.CategoryRepository;
import univ.rouen.categorymanagement.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        Category parent = null;
        if (dto.getParentId() != null) {
            parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent not found"));
            // E_CAT_90: cannot be its own parent
            // E_CAT_100: child cannot have two parents (JPA enforces 1 parent field)
        }

        Category category = new Category();
        category.setName(dto.getName());
        category.setParent(parent);
        categoryRepository.save(category);

        return mapToDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        existing.setName(dto.getName());
        // Handle parent changes if needed

        categoryRepository.save(existing);
        return mapToDto(existing);
    }

    @Override
    public void deleteCategory(Long id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        categoryRepository.delete(existing);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return mapToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories(int page, int size, String sort, String filter) {
        Sort sorting = Sort.by(sort).ascending();
        PageRequest pageRequest = PageRequest.of(page, size, sorting);
        Page<Category> categoryPage = categoryRepository.findAll(pageRequest);

        return categoryPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CategoryDto mapToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setParentId(category.getParent() != null ? category.getParent().getId() : null);
        dto.setChildrenIds(category.getChildren().stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
        dto.setIsRoot(category.getParent() == null);
        return dto;
    }
}
