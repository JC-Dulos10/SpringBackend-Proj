package com.auth.security.product;

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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        // Check if product with the same name already exists
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new ProductAlreadyExistsException("Product with name " + product.getProductName() + " already exists.");
        }
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product product) {
        Product existingProduct = getProductById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + productId + " not found."));

        // Update fields
        existingProduct.setProductName(product.getProductName());
        existingProduct.setProductPrice(product.getProductPrice());

        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }
}