package java02.group1.productcatalogmanagementsystem;

import java02.group1.productcatalogmanagementsystem.controller.AdminProductController;
import java02.group1.productcatalogmanagementsystem.dto.request.UpdateProductRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.ProductResponse;
import java02.group1.productcatalogmanagementsystem.service.CloudinaryService;
import java02.group1.productcatalogmanagementsystem.service.ProductService;
import java02.group1.productcatalogmanagementsystem.service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private CloudinaryService cloudinaryService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= CREATE PRODUCT =================
    @Test
    void createProduct_success() throws Exception {
        ProductResponse response = new ProductResponse();
        response.setId(1L);
        response.setName("Test Product");

        Mockito.when(productService.createProduct(Mockito.any()))
                .thenReturn(response);

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake image".getBytes()
        );

        mockMvc.perform(multipart("/api/admin/products")
                        .file(image)
                        .param("name", "Test Product")
                        .param("price", "100")
                        .param("stockQuantity", "10")
                        .param("categoryId", "1")
                        .param("description", "Test description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    // ================= UPDATE PRODUCT =================
    @Test
    void updateProduct_success() throws Exception {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setName("Updated Product");
        request.setPrice(200.0);
        request.setStockQuantity(20);

        ProductResponse response = new ProductResponse();
        response.setName("Updated Product");

        Mockito.when(productService.updateProduct(Mockito.eq(1L), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/admin/products/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    // ================= DELETE PRODUCT =================
    @Test
    void deleteProduct_success() throws Exception {
        Mockito.doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/admin/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Product deleted successfully"));
    }

    // ================= UPLOAD IMAGE =================
    @Test
    void uploadImage_success() throws Exception {
        Mockito.when(cloudinaryService.uploadImage(Mockito.any()))
                .thenReturn("http://cloudinary.com/test.jpg");

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake image".getBytes()
        );

        mockMvc.perform(multipart("/api/admin/products/upload-image")
                        .file(image))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl")
                        .value("http://cloudinary.com/test.jpg"));
    }
}
