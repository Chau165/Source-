package java02.group1.productcatalogmanagementsystem;

import java02.group1.productcatalogmanagementsystem.controller.PublicProductController;
import java02.group1.productcatalogmanagementsystem.dto.response.ProductResponse;
import java02.group1.productcatalogmanagementsystem.service.ProductService;
import java02.group1.productcatalogmanagementsystem.service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PublicProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= GET ACTIVE PRODUCTS =================
    @Test
    void getProducts_success() throws Exception {
        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setName("Iphone");
        product.setPrice(BigDecimal.valueOf(1000));

        Mockito.when(productService.getActiveProducts(null))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Iphone"))
                .andExpect(jsonPath("$[0].price").value(1000));
    }

    // ================= GET PRODUCT'S DETAILS =================
    @Test
    void getProductDetail_success() throws Exception {
        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setName("Iphone");

        Mockito.when(productService.getActiveProductById(1L))
                .thenReturn(product);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Iphone"));
    }
    
    // ================= FILTER BY CATEGORY ==================
    @Test
    void filterByCategory_success() throws Exception {
        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setName("Samsung");

        Mockito.when(productService.getActiveProducts(2L))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products/category/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Samsung"));
    }

    // ================= GET BY NAME =================
    @Test
    void searchByName_success() throws Exception {
        ProductResponse product = new ProductResponse();
        product.setName("Macbook");

        Mockito.when(productService.searchActiveProductsByName("mac"))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products/search")
                        .param("name", "mac"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Macbook"));
    }

}
