package com.jonathans.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jonathans.models.Alert;

@Repository
public interface AlertRepository extends JpaRepository<Alert, UUID> {
}
