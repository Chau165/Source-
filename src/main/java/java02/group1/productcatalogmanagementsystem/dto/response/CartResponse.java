package java02.group1.productcatalogmanagementsystem.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class CartResponse {
    private Long id;
    private Long accountId;
    private String accountUsername;
    private List<CartItemResponse> items;
    private double totalPrice;
}
