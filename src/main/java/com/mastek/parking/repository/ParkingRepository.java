package com.mastek.parking.repository;

import com.mastek.parking.model.Parking;
import com.mastek.parking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingRepository extends CrudRepository<Parking, Long> {
    List<Parking> findAll();

    @Query("SELECT p FROM Parking p WHERE p.location = :location  AND p.slotNumber = :slot_number")
    Optional<Parking> findByLocationAndSlot(@Param("location") String location, @Param("slot_number") Long slotNumber);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM Parking p WHERE p.location = :location AND is_occupied = false")
    boolean checkAvailability(@Param("location") String location);

    @Query("SELECT p FROM Parking p WHERE p.location = :location AND is_occupied = false")
    List<Parking> findAvailableParkingSlots(@Param("location") String location);
}
