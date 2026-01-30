package java02.group1.productcatalogmanagementsystem.repository;

import java02.group1.productcatalogmanagementsystem.entity.Account;
import java02.group1.productcatalogmanagementsystem.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByAccount(Account account);
}