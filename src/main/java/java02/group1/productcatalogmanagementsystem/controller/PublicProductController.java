package java02.group1.productcatalogmanagementsystem.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java02.group1.productcatalogmanagementsystem.dto.response.ProductResponse;
import java02.group1.productcatalogmanagementsystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SecurityRequirement(name = "api")
@Tag(name = "Products", description = "Public product endpoints")
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class PublicProductController {
    private final ProductService productService;

    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductResponse>> filterByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getActiveProducts(id));
    }

    // GET /api/products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts() {
        return ResponseEntity.ok(productService.getActiveProducts(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductDetail(
            @PathVariable Long id) {
        return ResponseEntity.ok(productService.getActiveProductById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchByName(
            @RequestParam String name) {
        return ResponseEntity.ok(productService.searchActiveProductsByName(name));
    }
}
