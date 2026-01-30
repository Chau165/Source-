package java02.group1.productcatalogmanagementsystem.repository;

import java.util.List;

import java02.group1.productcatalogmanagementsystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(String status);

    List<Product> findByStatusAndCategory_Id(String status, Long categoryId);

    List<Product> findByStatusAndNameContainingIgnoreCase(String status, String name);

    void deleteByCategory_Id(Long categoryId);
}
