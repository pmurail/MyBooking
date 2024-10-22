package org.example.mybooking.controller;

import org.example.mybooking.domain.FlightDetails;
import org.example.mybooking.domain.request.FlightRequest;
import org.example.mybooking.entity.FlightEntity;
import org.example.mybooking.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public List<FlightEntity> getFlights(@RequestBody FlightRequest request) {
        return flightService.getFlightsByDate(request);
    }

    @GetMapping("/details")
    public List<FlightDetails> getFlightsByDateWithDetails(@RequestBody FlightRequest request) {
        return flightService.getFlightsByDateWithDetails(request);
    }
}
