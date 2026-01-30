package java02.group1.productcatalogmanagementsystem.repository;

import java02.group1.productcatalogmanagementsystem.entity.Role;
import java02.group1.productcatalogmanagementsystem.entity.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleName> {

    Optional<Role> findByRoleName(RoleName roleName);

}
