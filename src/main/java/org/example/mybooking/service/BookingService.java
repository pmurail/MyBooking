package org.example.mybooking.service;

import org.example.mybooking.domain.request.ReservationRequest;
import org.example.mybooking.domain.request.ReservationsForDateRequest;
import org.example.mybooking.domain.response.ReservationResponse;
import org.example.mybooking.entity.BookingEntity;
import org.example.mybooking.entity.FlightEntity;
import org.example.mybooking.entity.PassengerEntity;
import org.example.mybooking.repository.BookingRepository;
import org.example.mybooking.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightService flightService;
    private final PassengerRepository passengerRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, FlightService flightService, PassengerRepository passengerRepository) {
        this.bookingRepository = bookingRepository;
        this.flightService = flightService;
        this.passengerRepository = passengerRepository;
    }

    public List<ReservationResponse> getBookingsByDate(ReservationsForDateRequest request) {
        List<ReservationResponse> reservations = new ArrayList<>();

        if (request.getDate() != null) {
            List<BookingEntity> bookings = bookingRepository.findByBookingDate(request.getDate());
            for (BookingEntity booking : bookings) {
                var reservation = ReservationResponse.builder()
                                                     .id(booking.getId())
                                                     .flightId(booking.getFlight().getId())
                                                     .date(booking.getBookingDate())
                                                     .passengerName(booking.getPassenger().getFirstName() + " " + booking.getPassenger().getLastName())
                                                     .build();
                reservations.add(reservation);
            }
        }

        return reservations;
    }

    public String createBooking(ReservationRequest reservation) {
        FlightEntity flight = flightService.getFlightById(reservation.getFlightId());
        if (flight == null || flightService.getNumberOfRemainingSeats(flight) <= 0) {
            return "The flight is not available.";
        }

        PassengerEntity passenger = passengerRepository.findById(reservation.getPassengerId()).get();

        BookingEntity booking = BookingEntity.builder()
                                             .flight(flight)
                                             .bookingDate(LocalDateTime.now())
                                             .passenger(passenger)
                                             .build();

        bookingRepository.save(booking);
        return "Booking created";
    }

    public String deleteBooking(Long id) {
        if (bookingRepository.existsById(id)) {
            bookingRepository.deleteById(id);
            return "Booking deleted";
        } else {
            return "Booking not found.";
        }
    }
}
