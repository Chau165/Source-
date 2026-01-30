package java02.group1.productcatalogmanagementsystem.repository;

import java02.group1.productcatalogmanagementsystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findAccountByAccountId(long accountId);
    Account findAccountByUsername(String username);

    boolean existsByUsername(String username);

}
