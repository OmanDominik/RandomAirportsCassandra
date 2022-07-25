package com.omanski.recruitment.service;

import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.repository.AirportsRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AirportService {

    final AirportsRepository airportsRepository;

    Logger logger = LoggerFactory.getLogger(AirportService.class);

    @CacheEvict(
            value = "airports",
            key = "'airportsList'"
    )
    public Airport save(Airport airport) {
        if (airportsRepository.findById(airport.get_id()).isEmpty()) {
            return airportsRepository.save(airport);
        } else
            throw new IllegalArgumentException("Airport with given id already exists");
    }

    @Cacheable(
            value = "airports",
            key = "'airportsList'")
    public List<Airport> getAirports() {
        logger.info("Retrieving all the airports...");
        return airportsRepository.findAll();
    }
}
