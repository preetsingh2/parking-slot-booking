package com.mastek.parking.service;

import com.mastek.parking.dto.ParkingDto;
import com.mastek.parking.exception.DuplicateParkingSlotException;
import com.mastek.parking.exception.ParkingNotFoundException;
import com.mastek.parking.exception.ResourceNotFoundException;
import com.mastek.parking.model.Parking;
import com.mastek.parking.repository.BookingRepository;
import com.mastek.parking.repository.ParkingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ParkingServiceImpl implements ParkingService{

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    @Transactional
    public Parking addParkingSlot(ParkingDto parkingDto) {

        Optional<Parking> existingSlot = parkingRepository
                .findByLocationAndSlot(parkingDto.getLocation(),parkingDto.getSlotNumber());
        if (existingSlot.isPresent()) {
            throw new DuplicateParkingSlotException(
                    "Parking slot " +parkingDto.getSlotNumber()+ " already exists for location: " + parkingDto.getLocation());
        }

        Parking parking = new Parking();
        parking.setLocation(parkingDto.getLocation());
        parking.setIsOccupied(parkingDto.getIs_occupied());
        parking.setSlotNumber(parkingDto.getSlotNumber());
        parking.setCreatedBy(parkingDto.getCreated_by());
         parkingRepository.save(parking);
         log.info("Parking added");

        return parking;
    }

    @Override
    @Transactional
    public List<Parking> getParking() {
        List<Parking> parkingList = parkingRepository.findAll();
        if (parkingList.isEmpty()) {
            throw new ParkingNotFoundException("Parking not found");
        }
        return parkingList;
    }
    @Override
    @Transactional
    public void deleteParkingSlot(Long parkingSlotNumber) {
        if (!parkingRepository.existsById(parkingSlotNumber)) {
            throw new ParkingNotFoundException("Parking slot not found with number: " + parkingSlotNumber);
        }
        parkingRepository.deleteById(parkingSlotNumber);
        log.info("Parking deleted successfully");
    }

}
