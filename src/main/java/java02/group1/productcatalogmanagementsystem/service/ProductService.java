package java02.group1.productcatalogmanagementsystem.service;

import java02.group1.productcatalogmanagementsystem.dto.request.ProductRequest;
import java02.group1.productcatalogmanagementsystem.dto.request.UpdateProductRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {

    List<ProductResponse> getActiveProducts(Long categoryId);

    ProductResponse getActiveProductById(Long id);

    List<ProductResponse> searchActiveProductsByName(String name);

    ProductResponse createProduct(ProductRequest dto);

    ProductResponse updateProduct(Long id, UpdateProductRequest req);

    void deleteProduct(Long id);

}
