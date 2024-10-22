package org.example.mybooking.service;

import org.example.mybooking.domain.FlightDetails;
import org.example.mybooking.domain.request.FlightRequest;
import org.example.mybooking.domain.request.ReservationsForDateRequest;
import org.example.mybooking.domain.response.ReservationResponse;
import org.example.mybooking.entity.BookingEntity;
import org.example.mybooking.entity.FlightEntity;
import org.example.mybooking.entity.PassengerEntity;
import org.example.mybooking.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    private BookingService bookingService;

    public List<FlightEntity> getFlightsByDate(FlightRequest request) {
        return flightRepository.findByDepartureTime(request.getDate());
    }

    public FlightEntity getFlightById(Long flightId) {
        return flightRepository.findById(flightId).orElse(null);
    }

    public List<FlightDetails> getFlightsByDateWithDetails(FlightRequest request) {
        List<FlightEntity> flights = flightRepository.findByDepartureTime(request.getDate());
        List<FlightDetails> flightDetails = new ArrayList<>();

        for (FlightEntity flight : flights) {
            List<ReservationResponse> reservations = bookingService.getBookingsByDate(new ReservationsForDateRequest(request.getDate()));

            int numberOfBookings = reservations.stream()
                                               .filter(reservation -> reservation.getFlightId().equals(flight.getId()))
                                               .toList()
                                               .size();

            FlightDetails details = FlightDetails.builder()
                                                 .flight(flight)
                                                 .numberOfBookings(numberOfBookings)
                                                 .numberOfAdults(getNumberOfAdults(flight))
                                                 .numberOfChildren(getNumberOfChildren(flight))
                                                 .numberOfBabies(getNumberOfBabies(flight))
                                                 .numberOfRemainingSeats(getNumberOfRemainingSeats(flight))
                                                 .build();
            flightDetails.add(details);
        }

        return flightDetails;
    }

    private int getNumberOfBabies(FlightEntity flight) {
        int numberOfBabies = 0;
        for (BookingEntity booking : flight.getBookings()) {
            PassengerEntity passenger = booking.getPassenger();
            int age = getAge(passenger.getBirthdayDate());
            if (age < 2) {
                numberOfBabies++;
            }
        }
        return numberOfBabies;
    }

    private int getNumberOfAdults(FlightEntity flight) {
        int numberOfAdults = 0;
        for (BookingEntity booking : flight.getBookings()) {
            PassengerEntity passenger = booking.getPassenger();
            int age = getAge(passenger.getBirthdayDate());
            if (age >= 12) {
                numberOfAdults++;
            }
        }
        return numberOfAdults;
    }

    private int getNumberOfChildren(FlightEntity flight) {
        int numberOfChildren = 0;
        for (BookingEntity booking : flight.getBookings()) {
            PassengerEntity passenger = booking.getPassenger();
            int age = getAge(passenger.getBirthdayDate());
            if (age >= 2 && age < 12) {
                numberOfChildren++;
            }
        }
        return numberOfChildren;
    }

    private int getAge(Date birthdayDate) {
        LocalDate birthDate = birthdayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    public int getNumberOfRemainingSeats(FlightEntity flight) {
        int capacity = flight.getAircraft().getCapacity();
        int numberOfBookings = flight.getBookings().size();
        int numberOfBabies = getNumberOfBabies(flight);
        return capacity - (numberOfBookings - numberOfBabies);
    }
}
