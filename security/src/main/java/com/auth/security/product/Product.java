package com.auth.security.product;

import com.auth.security.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    private float productPrice;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdDate; // Automatically populated when the entity is created

    @Column(nullable = false)
    private Long creatorUserId; // User ID who created the products

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public enum Status {
        ACTIVE,
        INACTIVE
    }


}
