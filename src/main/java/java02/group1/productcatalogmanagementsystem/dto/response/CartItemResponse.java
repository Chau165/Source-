package java02.group1.productcatalogmanagementsystem.dto.response;

import lombok.Data;

@Data
public class CartItemResponse {
    private Long Id;
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
    private double subTotal;
}
