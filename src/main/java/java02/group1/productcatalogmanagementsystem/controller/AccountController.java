package java02.group1.productcatalogmanagementsystem.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java02.group1.productcatalogmanagementsystem.dto.request.AccountRequest;
import java02.group1.productcatalogmanagementsystem.dto.request.LoginRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.AccountResponse;
import java02.group1.productcatalogmanagementsystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "api")
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@RequestBody @Valid AccountRequest request) {
        return ResponseEntity.ok(accountService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AccountResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(accountService.login(request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity<?> delete(@PathVariable long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Xóa tài khoản thành công");
    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    public ResponseEntity getCurrentAccount(){

        return ResponseEntity.ok(accountService.getCurrentAccount());
    }

}
