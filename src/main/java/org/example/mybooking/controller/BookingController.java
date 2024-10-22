package org.example.mybooking.controller;

import org.example.mybooking.domain.request.ReservationRequest;
import org.example.mybooking.domain.request.ReservationsForDateRequest;
import org.example.mybooking.domain.response.ReservationResponse;
import org.example.mybooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<ReservationResponse> getReservationsForDate(@RequestBody ReservationsForDateRequest request) {
        return bookingService.getBookingsByDate(request);
    }

    @PostMapping
    public String createBooking(@RequestBody ReservationRequest request) {
        return bookingService.createBooking(request);
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        return bookingService.deleteBooking(id);
    }
}
