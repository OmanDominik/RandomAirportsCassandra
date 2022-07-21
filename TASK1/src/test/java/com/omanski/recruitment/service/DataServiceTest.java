package com.omanski.recruitment.service;

import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.model.GeoPosition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DataServiceTest {

    @Mock
    AirportService airportService;

    @InjectMocks
    DataService dataService;


    @Test
    void generateJsons() {
        //given
        int sizeOfListToGenerate = 2;
        Airport sampleAirport = new Airport("uTUclIFUdn", 15, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                "EZP", "AwOFMFUW", "nIkaLW",
                new GeoPosition(-11.0f, -71.2f),
                468, false, "UX", false, 2614);

        Mockito.when(airportService.save(any(Airport.class)))
                .thenReturn(sampleAirport);

        try (MockedStatic<Airport> mockedStatic = Mockito.mockStatic(Airport.class)) {

            mockedStatic
                    .when(Airport::getRandomInstance)
                    .thenReturn(sampleAirport);

            //when
            List<Airport> generatedList = dataService.generateJsons(sizeOfListToGenerate);

            //then
            assertThat(generatedList.size()).isEqualTo(sizeOfListToGenerate);
            assertThat(generatedList).isEqualTo(List.of(sampleAirport, sampleAirport));
        }
    }
}