package univ.rouen.categorymanagement.service.impl;

import univ.rouen.categorymanagement.dto.CategoryDto;
import univ.rouen.categorymanagement.entity.Category;
import univ.rouen.categorymanagement.exception.ResourceNotFoundException;
import univ.rouen.categorymanagement.repository.CategoryRepository;
import univ.rouen.categorymanagement.repository.criteria.CategoryCriteria;
import univ.rouen.categorymanagement.repository.criteria.CategorySpecification;
import univ.rouen.categorymanagement.service.CategoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        }


        Category category = new Category();
        category.setName(dto.getName());
        category.setParent(parent);
        if (parent != null)
            incrementAncestorsDescendantsCount(category,1);
        categoryRepository.save(category);

        return mapToDto(category);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        existing.setName(dto.getName());
        // Handle parent changes if needed
        if (dto.getParentId() != null) {
            // Check for self-reference
            if (id.equals(dto.getParentId())) {
                throw new IllegalArgumentException("Category cannot be its own parent.");
            }

            // Check for descendant-parent reference
            if (isDescendant(dto.getParentId(), existing)) {
                throw new IllegalArgumentException("Category cannot be assigned as a parent of its descendant.");
            }

            // Fetch the new parent (if valid)
            Category newParent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));

            decrementAncestorsDescendantsCount(existing,existing.getDescendantsCount() + 1);
            existing.setParent(newParent);
            incrementAncestorsDescendantsCount(existing,existing.getDescendantsCount() + 1);

        } else {
            decrementAncestorsDescendantsCount(existing,existing.getDescendantsCount() + 1);
            existing.setParent(null); // Remove parent if parentId is null
        }

        // Update other fields
        existing.setName(dto.getName());

        categoryRepository.save(existing);
        return mapToDto(existing);
    }

    private boolean isDescendant(Long potentialParentId, Category category) {
        // Recursively check if potentialParentId is in the current category's descendants
        Category currentParent = category.getParent();
        while (currentParent != null) {
            if (currentParent.getId().equals(potentialParentId)) {
                return true;
            }
            currentParent = currentParent.getParent();
        }
        return false;
    }

    @Override
    public void deleteCategory(Long id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        decrementAncestorsDescendantsCount(existing,existing.getDescendantsCount() - 1); // Update descendants count of ancestors
        categoryRepository.delete(existing);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return mapToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories(int page, int size, CategoryCriteria criteria) {
        // 1) Build the specification for (name, date, isRoot)
        CategorySpecification spec = new CategorySpecification(criteria);
        //Sort sorting = Sort.by(sort).ascending(); // handle descending if needed
        PageRequest pageRequest = PageRequest.of(page, size);

        Page<Category> pageResult = categoryRepository.findAll(spec, pageRequest);

        List<Category> filtered = new ArrayList<>(pageResult.getContent());

        if (criteria.getDescendantsCount() != null) {
            filtered = filtered.stream()
                    .filter(cat ->cat.getDescendantsCount() == criteria.getDescendantsCount())
                    .toList();
        }
        if (criteria.getDateAfter() != null) {
            filtered = filtered.stream()
                   .filter(cat -> cat.getCreatedAt().isAfter(criteria.getDateAfter()))
                   .toList();
        }
        if (criteria.getDateBefore()!= null) {
            filtered = filtered.stream()
                   .filter(cat -> cat.getCreatedAt().isBefore(criteria.getDateBefore()))
                   .toList();
        }



        // 5) Convert to DTO
        return filtered.stream()
                .map(this::mapToDto)
                .toList();
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

    private void incrementAncestorsDescendantsCount(Category category, int amount) {
        Category current = category.getParent();
        while (current != null) {
            current.setDescendantsCount(current.getDescendantsCount() + amount);
            categoryRepository.save(current);  // update DB
            current = current.getParent();
        }
    }

    private void decrementAncestorsDescendantsCount(Category category, int amount) {
        Category current = category.getParent();
        while (current != null) {
            current.setDescendantsCount(current.getDescendantsCount() - amount);
            categoryRepository.save(current);
            current = current.getParent();
        }
    }


}
