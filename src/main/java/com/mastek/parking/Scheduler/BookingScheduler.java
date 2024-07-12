package com.mastek.parking.Scheduler;

import com.mastek.parking.dto.UpdateBookingDto;
import com.mastek.parking.exception.ParkingNotFoundException;
import com.mastek.parking.model.Booking;
import com.mastek.parking.model.Parking;
import com.mastek.parking.repository.BookingRepository;
import com.mastek.parking.repository.ParkingRepository;
import com.mastek.parking.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class BookingScheduler {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private BookingService bookingService;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void checkBookings() {
        log.info("checkBookings triggered");
        Date now = new Date();
        Timestamp currentTimestamp = new Timestamp(now.getTime());
        List<Booking> activeBookings = bookingRepository.findActiveBookings(currentTimestamp);
        Timestamp currentTimestamps = new Timestamp(System.currentTimeMillis());
        for (Booking booking : activeBookings) {
                if (booking.getBookingEndDateTime().before(currentTimestamps) && "Active".equals(booking.getBookingStatus())) {
                    // Update booking status to Completed
                    booking.setBookingStatus("Completed");
                    booking.setActualLeaveTime(now);
                    bookingRepository.save(booking);

                    // Update parking slot status
                    Parking parkingSlot = parkingRepository.findById(booking.getParkingSlotNumber())
                            .orElseThrow(() -> new ParkingNotFoundException("Parking slot not found"));

                    parkingSlot.setIsOccupied(false);
                    parkingRepository.save(parkingSlot);
            }
        }
    }

}
