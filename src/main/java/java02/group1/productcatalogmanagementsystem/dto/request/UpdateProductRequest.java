package java02.group1.productcatalogmanagementsystem.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {

    @NotBlank(message = "name is required")
    private String name;

    private String description;

    @NotNull(message = "price is required")
    @Min(value = 0, message = "price must be >= 0")
    private Double price;

    @NotNull(message = "stockQuantity is required")
    @Min(value = 0, message = "stockQuantity must be >= 0")
    private Integer stockQuantity;

    // ACTIVE / INACTIVE
    private String status;

    // Optional: URL ảnh sản phẩm (có thể lấy từ endpoint upload image)
    private String imageUrl;

    // Optional: bạn đang dùng FK column categories_id, update theo categoryId (id của Category)
    @Min(value = 1, message = "categoryId must be >= 1")
    private Long categoryId;
}
