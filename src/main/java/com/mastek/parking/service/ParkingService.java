package com.mastek.parking.service;

import com.mastek.parking.dto.ParkingDto;
import com.mastek.parking.model.Parking;

import java.util.List;


public interface ParkingService {

    Parking addParkingSlot(ParkingDto parkingDto);

    List<Parking> getParking();



}
