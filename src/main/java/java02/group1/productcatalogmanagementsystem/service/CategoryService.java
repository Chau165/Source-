package java02.group1.productcatalogmanagementsystem.service;

import java02.group1.productcatalogmanagementsystem.dto.request.CategoryRequest;
import java02.group1.productcatalogmanagementsystem.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    List<Category> getCategoriesByName(String name);

    Category getCategoryById(Long id);

    Category createCategory(CategoryRequest categoryRequest);

    Category updateCategory(Long id, CategoryRequest updatedCategory);

    void deleteCategory(Long id);

}