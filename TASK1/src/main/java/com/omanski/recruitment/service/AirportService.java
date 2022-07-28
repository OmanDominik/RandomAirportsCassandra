package com.omanski.recruitment.service;

import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.repository.AirportsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirportService {

    final AirportsRepository airportsRepository;

    @CacheEvict(
            value = "airports",
            key = "'airportsList'"
    )
    public Airport save(Airport airport) {
        if (airportsRepository.findById(airport.get_id()).isEmpty()) {
            log.info("Saving an airport in DB: {}", airport);
            return airportsRepository.save(airport);
        } else
            throw new IllegalArgumentException("Airport with given id already exists");
    }

    @Cacheable(
            value = "airports",
            key = "'airportsList'")
    public List<Airport> getAirports() {
        log.info("Retrieving all the airports from DB, recreating cache");
        return airportsRepository.findAll();
    }
}
