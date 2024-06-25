package com.project.demo.rest.product;


import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.product.Product;
import com.project.demo.logic.entity.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductRestController {

    @Autowired
    private ProductRepository ProductRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<Product> getAllProducts() {
        return ProductRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Product addProduct(@RequestBody Product product) {
        return ProductRepository.save(product);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return ProductRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/filterByName/{name}")
    public List<Product> getCategoryById(@PathVariable String name) {
        return ProductRepository.findProductsWithCharacterInName(name);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ProductRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setStockAmount(product.getStockAmount());
                    existingProduct.setCategory(product.getCategory());
                    return ProductRepository.save(existingProduct);
                })
                .orElseGet(() -> {
                    product.setId(id);
                    return ProductRepository.save(product);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public void deleteProduct(@PathVariable Long id) {
        ProductRepository.deleteById(id);
    }
}
