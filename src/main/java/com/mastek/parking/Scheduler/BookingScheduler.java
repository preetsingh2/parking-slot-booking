package com.mastek.parking.Scheduler;

import com.mastek.parking.dto.UpdateBookingDto;
import com.mastek.parking.model.Booking;
import com.mastek.parking.repository.BookingRepository;
import com.mastek.parking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class BookingScheduler {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;

    @Scheduled(fixedRate = 60000) // Run every minute
    public void checkBookings(UpdateBookingDto updateBookingDto) {
        Date now = new Date();
        List<Booking> activeBookings = bookingRepository.findActiveBookings(now);

        for (Booking booking : activeBookings) {
                if (booking.getBookingEndDateTime().before(now) && booking.getActualLeaveTime() == null ||
                    (booking.getBookingStatus() != null && booking.getBookingStatus().equals("Completed")) ) {
                updateBookingDto.setBookingId(booking.getBookingId());
                updateBookingDto.setActualLeaveTime(now);
                bookingService.updateBooking(updateBookingDto);
            }
        }
    }
}
