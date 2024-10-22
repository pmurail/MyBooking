package org.example.mybooking.repository;

import org.example.mybooking.entity.AircraftEntity;
import org.example.mybooking.entity.PassengerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<PassengerEntity, Long> {
}
