package java02.group1.productcatalogmanagementsystem.controller;

import jakarta.validation.Valid;
import java02.group1.productcatalogmanagementsystem.dto.request.CartItemRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.CartResponse;
import java02.group1.productcatalogmanagementsystem.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {
    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItemToCart(@Valid @RequestBody CartItemRequest request) {
        CartResponse response = cartService.addItemToCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        CartResponse response = cartService.getCart();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItemFromCart(@PathVariable Long cartItemId) {
        CartResponse response = cartService.removeItemFromCart(cartItemId);
        return ResponseEntity.ok(response);
    }

}
