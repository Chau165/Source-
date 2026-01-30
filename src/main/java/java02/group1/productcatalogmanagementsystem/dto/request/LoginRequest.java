package java02.group1.productcatalogmanagementsystem.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "username không được để trống")
    String username;

    @NotBlank(message = "password không được để trống")
    String password;

}
