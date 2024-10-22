package org.example.mybooking.repository;

import org.example.mybooking.entity.AircraftEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<AircraftEntity, Long> {
}
