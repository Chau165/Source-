package java02.group1.productcatalogmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "NVARCHAR(200)")
    private String name;

    @Column(columnDefinition = "NVARCHAR(1000)")
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false, length = 30)
    private String status = "ACTIVE";

    @Column
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categories_id", nullable = false)
    private Category category;

    @PrePersist
    void prePersist() {
        if (status == null || status.isBlank()) {
            status = "ACTIVE";
        }
    }
}
