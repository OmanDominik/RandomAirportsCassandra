package com.omanski.recruitment.repository;

import com.omanski.recruitment.model.Airport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AirportsRepository extends MongoRepository<Airport, Integer> {

}
