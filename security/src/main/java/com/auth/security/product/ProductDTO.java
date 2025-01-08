package com.auth.security.product;

import com.auth.security.category.Category;
import com.auth.security.category.CategoryDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private Long productId;
    private String productName;
    private float productPrice;
    private LocalDateTime createdDate;
    private Long creatorUserId;
    private CategoryDTO category;
    private String status; // Active or Inactive

    public ProductDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
        this.createdDate = product.getCreatedDate();
        this.creatorUserId = product.getCreatorUserId();
        this.status = product.getStatus().name();

        // Map category to CategoryDTO
        if (product.getCategory() != null) {
            this.category = new CategoryDTO(product.getCategory());
        }

    }
}

