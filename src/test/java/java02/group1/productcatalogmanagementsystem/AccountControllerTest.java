package java02.group1.productcatalogmanagementsystem;


import java02.group1.productcatalogmanagementsystem.controller.AccountController;
import java02.group1.productcatalogmanagementsystem.dto.request.AccountRequest;
import java02.group1.productcatalogmanagementsystem.dto.request.LoginRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.AccountResponse;
import java02.group1.productcatalogmanagementsystem.entity.entity.RoleName;
import java02.group1.productcatalogmanagementsystem.service.AccountService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private TokenService tokenService;

    @Autowired
    private ObjectMapper objectMapper;

    // ================= REGISTER =================
    @Test
    void register_success() throws Exception {
        AccountRequest request = new AccountRequest();
        request.setUsername("string");
        request.setPassword("string");
        request.setFullName("Test User");
        String role = "CUSTOMER";
        request.setRoleName(RoleName.valueOf(role));

        AccountResponse response = new AccountResponse();
        response.setUsername("string");

        Mockito.when(accountService.register(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/accounts/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("string"));
    }

    // ================= LOGIN =================
    @Test
    void login_success() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("string");
        request.setPassword("string");


        AccountResponse response = new AccountResponse();
        response.setUsername("string");

        Mockito.when(accountService.login(Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/accounts/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("string"));
    }

    // ================= DELETE =================
    @Test
    void deleteAccount_success() throws Exception {
        Mockito.doNothing().when(accountService).deleteAccount(1L);

        mockMvc.perform(delete("/api/accounts/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Xóa tài khoản thành công"));
    }

    // ================= CURRENT ACCOUNT =================
    @Test
    void getCurrentAccount_success() throws Exception {
        AccountResponse response = new AccountResponse();
        response.setUsername("string");

        Mockito.when(accountService.getCurrentAccount())
                .thenReturn(response);

        mockMvc.perform(get("/api/accounts/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("string"));
    }
}
