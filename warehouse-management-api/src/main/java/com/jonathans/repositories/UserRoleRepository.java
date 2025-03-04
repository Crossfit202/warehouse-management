package com.jonathans.repositories;

import com.jonathans.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
}
