package org.example.mybooking.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FLIGHT")
public class FlightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "DEPARTURE_AIRPORT")
    private String departureAirport;
    @Column(name = "ARRIVAL_AIRPORT")
    private String arrivalAirport;
    @Column(name = "DEPARTURE_TIME")
    private LocalDateTime departureTime;
    @Column(name = "ARRIVAL_TIME")
    private LocalDateTime arrivalTime;
    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;
    @Column(name = "AIRLINE")
    private String airline;
    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private AircraftEntity aircraft;
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
    private List<BookingEntity> bookings;
}

