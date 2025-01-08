package com.auth.security.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id " + id));
    }

    public Category createCategory(Category category) {
        if (category.getCreatorUserId() == null) {
            throw new IllegalArgumentException("Creator User ID cannot be null.");
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category category) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<CategoryDTO> getAllActiveCategories() {
        return categoryRepository.findByStatus(Category.Status.ACTIVE)
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getAllInactiveCategories() {
        return categoryRepository.findByStatus(Category.Status.INACTIVE)
                .stream()
                .map(CategoryDTO::new)
                .collect(Collectors.toList());
    }

    public void updateCategoryStatus(Long categoryId, Category.Status newStatus) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setStatus(newStatus);
        categoryRepository.save(category);
    }
}
