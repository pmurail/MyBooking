package org.example.mybooking.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.mybooking.entity.FlightEntity;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightDetails {

    private FlightEntity flight;
    private int numberOfBookings;
    private int numberOfAdults;
    private int numberOfChildren;
    private int numberOfBabies;
    private int numberOfRemainingSeats;
}
