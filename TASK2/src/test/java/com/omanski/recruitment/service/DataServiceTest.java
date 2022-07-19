package com.omanski.recruitment.service;

import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.model.GeoPosition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
@ActiveProfiles("test")
class DataServiceTest {

    @Value("${generating.url}")
    private String host;
    @Mock
    private RestTemplate restTemplate;

    @Spy
    @InjectMocks
    private DataService dataService = new DataService();

    @Test
    void shouldReturnRandomList() {
        //given
        int sizeOfListToGenerate = 2;
        Airport[] airportsList = {
                new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                        "EZP", "AwOFMFUW","nIkaLW",
                        new GeoPosition(-11.26171f, -71.221405f),
                        723086, false, "EB", false, 6988),
                new Airport("kscRyPiwpi", 47254818, 7852, "QyYTWOK", "xRywzQQoLnRFZurRVxQz",
                        "ARN", "GsPlTNoE","HVJbNx",
                        new GeoPosition(-64.0f, -71.0f),
                        276887, false, "WN", true, 17)
        };

        String uri = host + "generate/json/" + sizeOfListToGenerate;

        Mockito
                .doReturn(uri)
                .when(dataService).buildGeneratingUri(sizeOfListToGenerate);

        Mockito
                .when(restTemplate.getForEntity(uri, Airport[].class))
                .thenReturn(new ResponseEntity(airportsList, HttpStatus.OK));

        //when
        List<Airport> returnedList = dataService.generateJsons(sizeOfListToGenerate);

        //then
        assertThat(returnedList.size()).isEqualTo(sizeOfListToGenerate);
        assertThat(List.of(airportsList)).isEqualTo(returnedList);
    }

    @Test
    void shouldReturnListOfBasicData() {
        //given
        int sizeOfListToGenerate = 2;
        Airport[] airportsList = {
                new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                        "EZP", "AwOFMFUW","nIkaLW",
                        new GeoPosition(-11.26171f, -71.221405f),
                        723086, false, "EB", false, 6988),
                new Airport("kscRyPiwpi", 47254818, 7852, "QyYTWOK", "xRywzQQoLnRFZurRVxQz",
                        "ARN", "GsPlTNoE","HVJbNx",
                        new GeoPosition(-64.0f, -71.0f),
                        276887, false, "WN", true, 17)
        };

        Mockito
                .doReturn(List.of(airportsList))
                .when(dataService).generateJsons(sizeOfListToGenerate);

        //when
            //list with _type, _id, name, type, latitude, longitude
        List<String> returnedList = dataService.getBasicData(sizeOfListToGenerate);

        //then
        assertThat(returnedList.size()).isEqualTo(sizeOfListToGenerate);
        assertThat(returnedList.get(0)).isEqualTo("uTUclIFUdn,4563496,LeufsTo,AwOFMFUW,-11.26171,-71.221405");
        assertThat(returnedList.get(1)).contains("kscRyPiwpi");
    }

    @Test
    void shouldReturnListOfSpecifiedData() throws IllegalAccessException {
        //given
        int sizeOfListToGenerate = 3;
        Airport[] airportsList = {
                new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                        "EZP", "AwOFMFUW","nIkaLW",
                        new GeoPosition(-11.26171f, -71.221405f),
                        723086, false, "EB", false, 6988),
                new Airport("kscRyPiwpi", 47254818, 7852, "QyYTWOK", "xRywzQQoLnRFZurRVxQz",
                        "ARN", "GsPlTNoE","HVJbNx",
                        new GeoPosition(-64.0f, -71.0f),
                        276887, false, "WN", true, 17),
                new Airport("OSYBUckTNi", 12636970, 8802, "aQMfmKu", "xUTkERZoGvOAQdvqoWhD",
                        "BWW", "sDqBOnfo","oDdvEN",
                        new GeoPosition(-19.52464f, -84.685905f),
                        468830, false, "UX", false, 2614)
        };

        Mockito
                .doReturn(List.of(airportsList))
                .when(dataService).generateJsons(sizeOfListToGenerate);

        //when
        List<String> returnedList = dataService.getSpecifiedData(sizeOfListToGenerate, Arrays.asList("_id", "latitude", "longitude"));

        //then
        assertThat(returnedList.size()).isEqualTo(sizeOfListToGenerate);
        assertThat(returnedList).isEqualTo(Arrays.asList("4563496,-11.26171,-71.221405", "47254818,-64.0,-71.0", "12636970,-19.52464,-84.685905"));
    }

    @Test
    void shouldCalculateOperations() throws IllegalAccessException {
        //given
        int sizeOfListToGenerate = 1;
        Airport[] airportsList = {
                new Airport("uTUclIFUdn", 15, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                        "EZP", "AwOFMFUW","nIkaLW",
                        new GeoPosition(-11.0f, -71.2f),
                        468, false, "UX", false, 2614)
        };

        Mockito
                .doReturn(List.of(airportsList))
                .when(dataService).generateJsons(sizeOfListToGenerate);

        //when
        List<String> calculatedOperations = dataService.calculateGivenOperations(Arrays.asList("latitude*longitude", "T(Math).sqrt(location_id)", "distance-name", "key-(-_id)"));

        //then
        assertThat(calculatedOperations.size()).isEqualTo(4);
        assertThat(calculatedOperations.get(0)).isEqualTo("783.2");
        assertThat(calculatedOperations.get(1)).contains("21.63330765");
        assertThat(calculatedOperations.get(2)).contains("Illegal");
        assertThat(calculatedOperations.get(3)).isEqualTo("121.0");
    }
    
}