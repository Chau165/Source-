package java02.group1.productcatalogmanagementsystem.controller;

import jakarta.validation.Valid;
import java02.group1.productcatalogmanagementsystem.dto.request.CategoryRequest;
import java02.group1.productcatalogmanagementsystem.entity.Category;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java02.group1.productcatalogmanagementsystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "api")
@RequestMapping("/api/categories")
@RequiredArgsConstructor

public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Category>> getCategoriesByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getCategoriesByName(name));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> create(@RequestBody @Valid CategoryRequest category) {
        return ResponseEntity.ok(categoryService.createCategory(category));
    }

    @PutMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> update(
            @PathVariable Long id,
            @RequestBody @Valid CategoryRequest updatedCategory) {
        return ResponseEntity.ok(categoryService.updateCategory(id, updatedCategory));
    }

    @DeleteMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

}

