package com.omanski.recruitment.service;

import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.model.GeoPosition;
import com.omanski.recruitment.repository.AirportsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AirportServiceTest {

    @Mock
    AirportsRepository airportsRepository;

    @InjectMocks
    AirportService airportService;

    @Test
    void shouldSaveAirport() {
        //given
        Airport airport = new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                "EZP", "AwOFMFUW", "nIkaLW",
                new GeoPosition(-11.26171f, -71.221405f),
                723086, false, "EB", false, 6988);

        Mockito
                .when(airportsRepository.save(airport))
                .thenReturn(airport);
        Mockito
                .when(airportsRepository.findById(airport.get_id()))
                .thenReturn(Optional.empty());

        //when
        Airport returnedAirport = airportService.save(airport);

        //then
        assertThat(returnedAirport).isEqualTo(airport);
        verify(airportsRepository).save(airport);
        verify(airportsRepository).findById(airport.get_id());
    }

    @Test
    void shouldNotSaveAirport() {
        //given
        Airport airport = new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                "EZP", "AwOFMFUW", "nIkaLW",
                new GeoPosition(-11.26171f, -71.221405f),
                723086, false, "EB", false, 6988);

        Mockito
                .when(airportsRepository.findById(airport.get_id()))
                .thenReturn(Optional.of(airport));

        //when
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            airportService.save(airport);
        });

        //then
        Assertions.assertEquals("Airport with given id already exists", thrown.getMessage());
        verify(airportsRepository, never()).save(airport);
        verify(airportsRepository).findById(airport.get_id());
    }

    @Test
    void shouldReturnAirports() {
        //given
        List<Airport> airportsList = Arrays.asList(
                new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                        "EZP", "AwOFMFUW", "nIkaLW",
                        new GeoPosition(-11.26171f, -71.221405f),
                        723086, false, "EB", false, 6988),
                new Airport("kscRyPiwpi", 47254818, 7852, "QyYTWOK", "xRywzQQoLnRFZurRVxQz",
                        "ARN", "GsPlTNoE", "HVJbNx",
                        new GeoPosition(-64.0f, -71.0f),
                        276887, false, "WN", true, 17),
                new Airport("OSYBUckTNi", 12636970, 8802, "aQMfmKu", "xUTkERZoGvOAQdvqoWhD",
                        "BWW", "sDqBOnfo", "oDdvEN",
                        new GeoPosition(-19.52464f, -84.685905f),
                        468830, false, "UX", false, 2614)
        );

        Mockito
                .when(airportsRepository.findAll())
                .thenReturn(airportsList);

        //when
        List<Airport> airportsFromDB = airportService.getAirports();

        //then
        assertThat(airportsFromDB.size()).isEqualTo(airportsList.size());
        assertThat(airportsFromDB).isEqualTo(airportsList);
        verify(airportsRepository).findAll();
    }
}