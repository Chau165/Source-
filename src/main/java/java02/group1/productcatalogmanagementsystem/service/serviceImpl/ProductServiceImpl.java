package java02.group1.productcatalogmanagementsystem.service.serviceImpl;

import java02.group1.productcatalogmanagementsystem.dto.request.ProductRequest;
import java02.group1.productcatalogmanagementsystem.dto.request.UpdateProductRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.ProductResponse;
import java02.group1.productcatalogmanagementsystem.entity.Category;
import java02.group1.productcatalogmanagementsystem.entity.Product;
import java02.group1.productcatalogmanagementsystem.repository.CartItemRepository;
import java02.group1.productcatalogmanagementsystem.repository.CategoryRepository;
import java02.group1.productcatalogmanagementsystem.repository.ProductRepository;
import java02.group1.productcatalogmanagementsystem.service.CloudinaryService;
import java02.group1.productcatalogmanagementsystem.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<ProductResponse> getActiveProducts(Long categoryId) {
        if (categoryId != null) {
            categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

            return productRepository.findByStatusAndCategory_Id("ACTIVE", categoryId)
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }

        return productRepository.findByStatus("ACTIVE")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse getActiveProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
            throw new EntityNotFoundException("Product not found");
        }

        return toResponse(product);
    }

    @Override
    public List<ProductResponse> searchActiveProductsByName(String name) {
        return productRepository
                .findByStatusAndNameContainingIgnoreCase("ACTIVE", name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse createProduct(ProductRequest dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + dto.getCategoryId()));

        String imageUrl = (dto.getImage() != null && !dto.getImage().isEmpty())
                ? cloudinaryService.uploadImage(dto.getImage())
                : null;

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        product.setStatus("ACTIVE");

        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, UpdateProductRequest req) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        // Validate & update stockQuantity (chỉ set ở đây)
        if (req.getStockQuantity() != null) {
            Long totalInCarts = cartItemRepository.getTotalQuantityInCarts(id);

            if (req.getStockQuantity() < totalInCarts) {
                throw new IllegalArgumentException(
                        "Không thể cập nhật số lượng kho xuống " + req.getStockQuantity() +
                                ". Hiện đang có " + totalInCarts + " sản phẩm nằm trong giỏ hàng của khách.");
            }
            product.setStockQuantity(req.getStockQuantity());
        }

        if (req.getCategoryId() != null) {
            Category category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Category not found with id: " + req.getCategoryId()));
            product.setCategory(category);
        }

        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());

        // Update imageUrl if provided
        if (req.getImageUrl() != null && !req.getImageUrl().isBlank()) {
            product.setImageUrl(req.getImageUrl().trim());
        }

        if (req.getStatus() != null && !req.getStatus().isBlank()) {
            product.setStatus(req.getStatus().trim().toUpperCase());
        }

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        // Chặn xoá nếu sản phẩm đang nằm trong giỏ
        if (cartItemRepository.existsByProduct_Id(id)) {
            throw new IllegalArgumentException("Không thể xóa sản phẩm này vì đang có khách hàng thêm vào giỏ hàng.");
        }

        if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
            throw new EntityNotFoundException("Product is not ACTIVE or already deleted");
        }

        product.setStatus("INACTIVE");
        productRepository.save(product);
    }

    private ProductResponse toResponse(Product p) {
        ProductResponse resp = modelMapper.map(p, ProductResponse.class);

        if (p.getCategory() != null) {
            resp.setCategoryId(p.getCategory().getId());
            resp.setCategoryName(p.getCategory().getName());
        } else {
            resp.setCategoryId(null);
            resp.setCategoryName(null);
        }

        if (resp.getPrice() == null) {
            resp.setPrice(java.math.BigDecimal.valueOf(p.getPrice()));
        }

        resp.setOutOfStock(p.getStockQuantity() == 0);

        return resp;
    }
}
