package java02.group1.productcatalogmanagementsystem.service.serviceImpl;

import java02.group1.productcatalogmanagementsystem.dto.request.CategoryRequest;
import java02.group1.productcatalogmanagementsystem.entity.Category;
import java02.group1.productcatalogmanagementsystem.service.CategoryService;
import jakarta.transaction.Transactional;
import java02.group1.productcatalogmanagementsystem.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java02.group1.productcatalogmanagementsystem.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    @Transactional
    public Category createCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, CategoryRequest updatedCategory) {
        Category existingCategory = getCategoryById(id);
        existingCategory.setName(updatedCategory.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        productRepository.deleteByCategory_Id(id);
        categoryRepository.delete(category);
    }
}
