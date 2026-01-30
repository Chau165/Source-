package java02.group1.productcatalogmanagementsystem.service;

import java02.group1.productcatalogmanagementsystem.dto.request.CartItemRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.CartResponse;
import org.springframework.stereotype.Service;

public interface CartService {
    CartResponse addItemToCart(CartItemRequest request);
    CartResponse getCart();
    CartResponse removeItemFromCart(Long cartItemId);
}
