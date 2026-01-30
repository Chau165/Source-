package java02.group1.productcatalogmanagementsystem;


import java02.group1.productcatalogmanagementsystem.controller.CategoryController;
import java02.group1.productcatalogmanagementsystem.dto.request.CategoryRequest;
import java02.group1.productcatalogmanagementsystem.entity.Category;
import java02.group1.productcatalogmanagementsystem.service.CategoryService;
import java02.group1.productcatalogmanagementsystem.service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= GET ALL =================
    @Test
    void getAllCategories_success() throws Exception {
        Category c1 = new Category();
        c1.setId(1L);
        c1.setName("Electronics");

        Category c2 = new Category();
        c2.setId(2L);
        c2.setName("Clothes");

        Mockito.when(categoryService.getAllCategories())
                .thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    // ================= GET BY NAME =================
    @Test
    void getCategoriesByName_success() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Mockito.when(categoryService.getCategoriesByName("Electronics"))
                .thenReturn(List.of(category));

        mockMvc.perform(get("/api/categories/name/{name}", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"));
    }

    // ================= GET BY ID =================
    @Test
    void getCategoryById_success() throws Exception {
        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        Mockito.when(categoryService.getCategoryById(1L))
                .thenReturn(category);

        mockMvc.perform(get("/api/categories/id/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    // ================= CREATE =================
    @Test
    void createCategory_success() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Books");

        Category category = new Category();
        category.setId(1L);
        category.setName("Books");

        Mockito.when(categoryService.createCategory(Mockito.any()))
                .thenReturn(category);

        mockMvc.perform(post("/api/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Books"));
    }

    // ================= UPDATE =================
    @Test
    void updateCategory_success() throws Exception {
        CategoryRequest request = new CategoryRequest();
        request.setName("Updated Category");

        Category category = new Category();
        category.setId(1L);
        category.setName("Updated Category");

        Mockito.when(categoryService.updateCategory(Mockito.eq(1L), Mockito.any()))
                .thenReturn(category);

        mockMvc.perform(put("/api/categories/id/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Category"));
    }

    // ================= DELETE =================
    @Test
    void deleteCategory_success() throws Exception {
        Mockito.doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/id/{id}", 1L))
                .andExpect(status().isOk());
    }
}
