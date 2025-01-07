package com.auth.security.product;

import com.auth.security.category.CategoryDTO;
import com.auth.security.exception.ProductAlreadyExistsException;
import com.auth.security.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private String normalizeProductName(String productName) {
        return productName.toLowerCase().replace(" ", "");
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        if (product.getCreatorUserId() == null) {
            throw new IllegalArgumentException("Creator User ID cannot be null.");
        }
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new ProductAlreadyExistsException("Product with name " + product.getProductName() + " already exists.");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product product) {
        Product existingProduct = getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found."));

        if (productRepository.existsByProductName(normalizeProductName(product.getProductName()))) {
            throw new ProductAlreadyExistsException("Product with name " + product.getProductName() + " already exists.");
        }
        existingProduct.setProductName(product.getProductName());
        existingProduct.setProductPrice(product.getProductPrice());
        existingProduct.setCategory(product.getCategory());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

}