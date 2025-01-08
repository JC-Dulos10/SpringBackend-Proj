package com.auth.security.category;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdDate;
    private String status; // Active or Inactive

    public CategoryDTO(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.createdDate = category.getCreatedDate();
        this.status = category.getStatus().name();
    }
}

