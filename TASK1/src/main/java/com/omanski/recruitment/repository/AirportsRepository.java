package com.omanski.recruitment.repository;

import com.omanski.recruitment.model.Airport;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportsRepository extends CassandraRepository<Airport, Integer> {

}
