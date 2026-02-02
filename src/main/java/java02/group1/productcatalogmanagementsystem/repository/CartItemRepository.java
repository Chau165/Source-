package java02.group1.productcatalogmanagementsystem.repository;

import java02.group1.productcatalogmanagementsystem.entity.Cart;
import java02.group1.productcatalogmanagementsystem.entity.CartItem;
import java02.group1.productcatalogmanagementsystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    @Query("SELECT COALESCE(SUM(ci.subTotal), 0) FROM CartItem ci WHERE ci.cart.id = :cartId")
    double sumSubTotalByCartId(@Param("cartId") Long cartId);

    @Query("SELECT COALESCE(SUM(ci.quantity), 0) FROM CartItem ci WHERE ci.product.id = :productId")
    Long getTotalQuantityInCarts(@Param("productId") Long productId);

    boolean existsByProduct_Id(Long productId);

    boolean existsByProduct_Category_Id(Long categoryId);

}
