package com.omanski.recruitment.service;

import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.repository.AirportsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AirportService {

    final AirportsRepository airportsRepository;

    public Airport save(Airport airport) {
        if(airportsRepository.findById(airport.get_id()).isEmpty()){
            return airportsRepository.save(airport);
        }
        else
            return null;
    }
}
