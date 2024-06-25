package com.project.demo.rest.category;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryRestController {

    @Autowired
    private CategoryRepository CategoryRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<Category> getAllCategories() {
        return CategoryRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Category addCategory(@RequestBody Category category) {
        return CategoryRepository.save(category);
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return CategoryRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/filterByName/{name}")
    public List<Category> getCategoryById(@PathVariable String name) {
        return CategoryRepository.findCategoryWithCharacterInName(name);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return CategoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    existingCategory.setDescription(category.getDescription());
                    return CategoryRepository.save(existingCategory);
                })
                .orElseGet(() -> {
                    category.setId(id);
                    return CategoryRepository.save(category);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        CategoryRepository.deleteById(id);
    }
}
