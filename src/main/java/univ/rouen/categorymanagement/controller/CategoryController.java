package univ.rouen.categorymanagement.controller;

import univ.rouen.categorymanagement.dto.CategoryDto;
import univ.rouen.categorymanagement.exception.ResourceNotFoundException;
import univ.rouen.categorymanagement.repository.criteria.CategoryCriteria;
import univ.rouen.categorymanagement.service.CategoryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {
        CategoryDto created = categoryService.createCategory(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody CategoryDto dto) {
        CategoryDto updated = categoryService.updateCategory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> get(@PathVariable Long id) {
        CategoryDto found = categoryService.getCategory(id);
        return ResponseEntity.ok(found);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String dateAfter,
            @RequestParam(required = false) String dateBefore,
            @RequestParam(required = false) Boolean isRoot,
            @RequestParam(required = false) Integer descendantsCount
    ) {
        // 1) Build the criteria object
        CategoryCriteria criteria = new CategoryCriteria();
        criteria.setSearch(search);
        try {
            // parse dateAfter/dateBefore to LocalDate
            if (dateAfter != null && !dateAfter.isEmpty()) {
                criteria.setDateAfter(LocalDateTime.parse(dateAfter));
            }
            if (dateBefore != null && !dateBefore.isEmpty()) {
                    criteria.setDateAfter(LocalDateTime.parse(dateBefore));
            }
        }
        catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: timestamp(yyyy-MM-dd'T'HH:mm:ss.SSSSSS)");
        }

        criteria.setIsRoot(isRoot);
        criteria.setDescendantsCount(descendantsCount);


        List<CategoryDto> categories = categoryService.getAllCategories(page, size, criteria);
        return ResponseEntity.ok(categories);
    }
}
