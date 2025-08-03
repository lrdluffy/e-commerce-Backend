package com.strawhats.ecommercebackend.repository;

import com.strawhats.ecommercebackend.model.AppRole;
import com.strawhats.ecommercebackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findRoleByRoleName(AppRole roleName);
}
