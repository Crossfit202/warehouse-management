package com.jonathans.repositories;

import com.jonathans.models.StorageLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface StorageLocationRepository extends JpaRepository<StorageLocation, UUID> {
    Optional<StorageLocation> findByName(String name);
    boolean existsByName(String name);
}
