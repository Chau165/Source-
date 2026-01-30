package java02.group1.productcatalogmanagementsystem.controller;

import jakarta.validation.Valid;
import java02.group1.productcatalogmanagementsystem.dto.request.ProductRequest;
import java02.group1.productcatalogmanagementsystem.dto.request.UpdateProductRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.ProductResponse;
import java02.group1.productcatalogmanagementsystem.service.ProductService;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@Tag(name = "Admin Products", description = "Administrative product management endpoints")
@SecurityRequirement(name = "api")
@RequestMapping("/api/admin/products")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;
    private final java02.group1.productcatalogmanagementsystem.service.CloudinaryService cloudinaryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @ModelAttribute ProductRequest productDTO) {

        return ResponseEntity.ok(productService.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(Collections.singletonMap("message", "Product deleted successfully"));
    }

    /**
     * Upload image to Cloudinary and return the URL
     * Use this URL for updating product image via updateProduct endpoint
     */
    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> uploadImage(
            @RequestParam("image") org.springframework.web.multipart.MultipartFile image) {
        String imageUrl = cloudinaryService.uploadImage(image);
        return ResponseEntity.ok(Collections.singletonMap("imageUrl", imageUrl));
    }
}
