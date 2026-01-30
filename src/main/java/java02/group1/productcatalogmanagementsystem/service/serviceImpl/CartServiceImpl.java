package java02.group1.productcatalogmanagementsystem.service.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java02.group1.productcatalogmanagementsystem.dto.request.CartItemRequest;
import java02.group1.productcatalogmanagementsystem.dto.response.CartItemResponse;
import java02.group1.productcatalogmanagementsystem.dto.response.CartResponse;
import java02.group1.productcatalogmanagementsystem.entity.Account;
import java02.group1.productcatalogmanagementsystem.entity.Cart;
import java02.group1.productcatalogmanagementsystem.entity.CartItem;
import java02.group1.productcatalogmanagementsystem.entity.Product;
import java02.group1.productcatalogmanagementsystem.repository.CartItemRepository;
import java02.group1.productcatalogmanagementsystem.repository.CartRepository;
import java02.group1.productcatalogmanagementsystem.repository.ProductRepository;
import java02.group1.productcatalogmanagementsystem.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CartResponse addItemToCart(CartItemRequest request) {
        Account account = getCurrentAccount();

        // Check product
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
            throw new IllegalArgumentException("Product is not available");
        }

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        // Get or create cart
        Cart cart = cartRepository.findByAccount(account)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setAccount(account);
                    newCart.setTotalPrice(0.0);
                    return cartRepository.save(newCart);
                });

        // Check if item already in cart
        Optional<CartItem> existing = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        int newTotalQuantity = request.getQuantity();

        if (existing.isPresent()) {
            newTotalQuantity += existing.get().getQuantity();
        }

        if (newTotalQuantity > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Not enough stock. Available: " + product.getStockQuantity()
            );
        }

        if (existing.isPresent()) {
            // Update quantity
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setSubTotal(item.getPrice() * item.getQuantity());
            cartItemRepository.save(item);
        } else {
            // Add new item
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            item.setPrice(product.getPrice());
            item.setSubTotal(product.getPrice() * request.getQuantity());
            cartItemRepository.save(item);
        }

        updateTotal(cart);

        return toResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        Account account = getCurrentAccount();

        Cart cart = cartRepository.findByAccount(account)
                .orElseGet(() -> {
                    Cart empty = new Cart();
                    empty.setAccount(account);
                    empty.setTotalPrice(0.0);
                    return empty;
                });

        return toResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse removeItemFromCart(Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        Cart cart = item.getCart();
        cartItemRepository.delete(item);

        updateTotal(cart);

        return toResponse(cart);
    }

    private Account getCurrentAccount() {
        return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void updateTotal(Cart cart) {
        double total = cartItemRepository.sumSubTotalByCartId(cart.getId());
        cart.setTotalPrice(total);
        cartRepository.save(cart);
    }

    private CartResponse toResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setAccountId(cart.getAccount().getAccountId());
        response.setAccountUsername(cart.getAccount().getUsername());
        response.setTotalPrice(cart.getTotalPrice());

        response.setItems(cart.getCartItems().stream()
                .map(item -> {
                    CartItemResponse itemResponse = new CartItemResponse();
                    itemResponse.setId(item.getId());
                    itemResponse.setProductId(item.getProduct().getId());
                    itemResponse.setProductName(item.getProduct().getName());
                    itemResponse.setQuantity(item.getQuantity());
                    itemResponse.setPrice(item.getPrice());
                    itemResponse.setSubTotal(item.getSubTotal());
                    return itemResponse;
                })
                .toList());

        return response;
    }
}
