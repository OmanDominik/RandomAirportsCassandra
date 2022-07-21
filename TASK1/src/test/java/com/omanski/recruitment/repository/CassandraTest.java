package com.omanski.recruitment.repository;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.model.GeoPosition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class CassandraTest {

    private static final String KEYSPACE_NAME = "test";

    @Container
    public static final CassandraContainer cassandra = (CassandraContainer) new CassandraContainer("cassandra:3.11.2").withExposedPorts(9042);


    @BeforeAll
    static void setupCassandraConnectionProperties() {
        System.setProperty("spring.data.cassandra.keyspace-name", KEYSPACE_NAME);
        System.setProperty("spring.data.cassandra.contact-points", cassandra.getContainerIpAddress());
        System.setProperty("spring.data.cassandra.port", String.valueOf(cassandra.getMappedPort(9042)));
        createKeyspace(cassandra.getCluster());
    }

    static void createKeyspace(Cluster cluster) {
        try (Session session = cluster.connect()) {
            session.execute("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE_NAME +
                    " WITH replication = \n" +
                    "{'class':'SimpleStrategy','replication_factor':'1'};");
        }
    }

    @Nested
    class ApplicationContextTest {
        @Test
        void givenCassandraContainer_whenSpringContextIsBootstrapped_thenContainerIsRunningWithNoExceptions() {
            assertThat(cassandra.isRunning()).isTrue();
        }
    }

    @Nested
    class AirportsRepositoryTest{

        @Autowired
        private AirportsRepository airportsRepository;

        @Test
        void shouldAddAirport() {
            //given
            Airport airport = new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                    "EZP", "AwOFMFUW", "nIkaLW",
                    new GeoPosition(-11.26171f, -71.221405f),
                    723086, false, "EB", false, 6988);


            //when
            airportsRepository.save(airport);
            List<Airport> savedAirports = airportsRepository.findAll();

            //then
            assertThat(savedAirports.get(0)).isEqualTo(airport);
        }

        @Test
        void shouldNotAddAirport() {
            //given
            Airport airport = new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                    "EZP", "AwOFMFUW", "nIkaLW",
                    new GeoPosition(-11.26171f, -71.221405f),
                    723086, false, "EB", false, 6988);


            //when
            airportsRepository.save(airport);
            List<Airport> savedAirports = airportsRepository.findAll();

            //then
            assertThat(savedAirports.size()).isEqualTo(1);
        }

        @Test
        void shouldFindInsertedAirport() {
            //given
            Airport airport = new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                    "EZP", "AwOFMFUW", "nIkaLW",
                    new GeoPosition(-11.26171f, -71.221405f),
                    723086, false, "EB", false, 6988);
            //when
            Optional isFound = airportsRepository.findById(airport.get_id());

            //then
            assertThat(isFound.isEmpty()).isFalse();
        }
    }

}