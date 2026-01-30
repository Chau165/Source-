package java02.group1.productcatalogmanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity//đánh dấu là 1 thực thể
@Data//tự động sinh getter setter
@Table(name = "accounts") //tên bảng trong DB
@AllArgsConstructor//tự động sinh constructor có tham số
@NoArgsConstructor//tự động sinh constructor không tham số
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private long accountId;

    @Column(name = "username", nullable = false, unique = true, columnDefinition = "NVARCHAR(255)")
    @NotEmpty(message = "username cannot be empty!")
    private String username;

    @Column(name = "password",nullable = false)
    @NotEmpty(message = "password cannot be empty!")
    private String password;

    @Column(name = "full_name", nullable = false, length = 100,  columnDefinition = "NVARCHAR(255)")
    @NotEmpty(message = "Full name cannot be empty!")
    private String fullName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_name", nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name())
        );
    }
}
