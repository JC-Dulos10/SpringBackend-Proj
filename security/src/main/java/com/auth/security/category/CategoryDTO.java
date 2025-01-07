package com.auth.security.category;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdDate;

    public CategoryDTO(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.createdDate = category.getCreatedDate();
    }
}

