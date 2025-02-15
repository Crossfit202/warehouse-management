package com.jonathans.repositories;

import com.jonathans.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    // ✅ Ensure this method correctly queries for a single Role by its name
    Optional<Role> findByRoleName(String roleName);
}
