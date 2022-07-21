package com.omanski.recruitment.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DataServiceTest {
//
//    @MockBean
//    AirportsRepository airportsRepository;
//
//    @MockBean
//    AirportService airportService = new AirportService(airportsRepository);
//
//    @InjectMocks
//    DataService dataService = new DataService(airportService);
//

//    @Test
//    void generateJsons() {
//        //given
//        int sizeOfListToGenerate = 2;
//        Airport sampleAirport = new Airport("uTUclIFUdn", 15, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
//                "EZP", "AwOFMFUW", "nIkaLW",
//                new GeoPosition(-11.0f, -71.2f),
//                468, false, "UX", false, 2614);
//
//
//        try (MockedStatic<Airport> mockedStatic = Mockito.mockStatic(Airport.class)) {
//
//            mockedStatic
//                    .when(Airport::getRandomInstance)
//                    .thenReturn(sampleAirport);
//            Mockito
//                    .when(airportService.save(sampleAirport))
//                    .thenReturn(sampleAirport);
//
//
//            //when
//            List<Airport> generatedList = dataService.generateJsons(sizeOfListToGenerate);
//
//            //then
//            assertThat(generatedList.size()).isEqualTo(sizeOfListToGenerate);
//            assertThat(generatedList).isEqualTo(List.of(sampleAirport, sampleAirport));
//        }
//    }
}