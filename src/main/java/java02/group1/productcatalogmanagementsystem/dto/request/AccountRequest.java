package java02.group1.productcatalogmanagementsystem.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java02.group1.productcatalogmanagementsystem.entity.entity.RoleName;
import lombok.Data;

@Data
public class AccountRequest {

    @NotEmpty(message = "username cannot be empty!")
    private String username;

    @NotEmpty(message = "password cannot be empty!")
    private String password;

    @NotEmpty(message = "Full name cannot be empty!")
    private String fullName;

    @NotNull(message = "roleName cannot be null!")
    private RoleName roleName;

}
