package java02.group1.productcatalogmanagementsystem.dto.response;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java02.group1.productcatalogmanagementsystem.entity.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccountResponse {

    private long accountId;
    private String username;
    private String token;
    private String fullName;

    private String roleName;
    private LocalDateTime createdAt;

}
