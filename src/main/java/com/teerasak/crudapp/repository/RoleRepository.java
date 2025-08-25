package com.teerasak.crudapp.repository;

import com.teerasak.crudapp.entitity.Role;
import com.teerasak.crudapp.entitity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRoleName(RoleName roleName);
}
