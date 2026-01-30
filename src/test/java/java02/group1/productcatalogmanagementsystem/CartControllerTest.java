package java02.group1.productcatalogmanagementsystem;

import java02.group1.productcatalogmanagementsystem.controller.CartController;
import java02.group1.productcatalogmanagementsystem.dto.request.CartItemRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.CartResponse;
import java02.group1.productcatalogmanagementsystem.service.CartService;
import java02.group1.productcatalogmanagementsystem.service.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= ADD CART ITEMs =================
    @Test
    @WithMockUser(username = "testuser", roles = {"CUSTOMER"})
    void addItemToCart_success() throws Exception {
        CartItemRequest request = new CartItemRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        CartResponse response = new CartResponse();
        response.setId(1L);
        response.setAccountId(10L);
        response.setAccountUsername("testuser");
        response.setTotalPrice(200.0);
        response.setItems(List.of());

        Mockito.when(cartService.addItemToCart(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/cart/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountUsername").value("testuser"))
                .andExpect(jsonPath("$.totalPrice").value(200.0));
    }

    // ================= GET CART =================
    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void getCart_success() throws Exception {

        CartResponse response = new CartResponse();
        response.setId(1L);
        response.setAccountId(10L);
        response.setAccountUsername("testuser");
        response.setTotalPrice(300.0);
        response.setItems(List.of());

        Mockito.when(cartService.getCart())
                .thenReturn(response);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.accountUsername").value("testuser"))
                .andExpect(jsonPath("$.totalPrice").value(300.0));
    }

    // ================= REMOVE ITEM =================
    @Test
    @WithMockUser(username = "testuser", roles = "CUSTOMER")
    void removeItemFromCart_success() throws Exception {

        CartResponse response = new CartResponse();
        response.setId(1L);
        response.setAccountId(10L);
        response.setAccountUsername("testuser");
        response.setTotalPrice(100.0);
        response.setItems(List.of());

        Mockito.when(cartService.removeItemFromCart(1L))
                .thenReturn(response);

        mockMvc.perform(
                        org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .delete("/api/cart/items/{id}", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(100.0));
    }
}
