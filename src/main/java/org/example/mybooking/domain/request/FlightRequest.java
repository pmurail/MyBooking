package org.example.mybooking.domain.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FlightRequest {
    private LocalDateTime date;
}
