package com.omanski.recruitment.service;

import com.omanski.recruitment.model.Airport;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DataService {

    final AirportService airportService;

    public List<Airport> generateJsons(int size) {

        List<Airport> generatedList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Airport randomAirport = Airport.getRandomInstance();
            generatedList.add(randomAirport);
            airportService.save(randomAirport);
        }

        return generatedList;
    }


}
