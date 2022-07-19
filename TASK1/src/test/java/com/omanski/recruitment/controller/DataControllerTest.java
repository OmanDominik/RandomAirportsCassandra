package com.omanski.recruitment.controller;

import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.model.GeoPosition;
import com.omanski.recruitment.service.DataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DataController.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = DataController.class)
class DataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DataService dataService;

    @InjectMocks
    private DataController dataController = new DataController(dataService);

    @Test
    void shouldReturnGeneratedList() throws Exception {
        //given
        int sizeOfListToGenerate = 3;
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
                .when(dataService.generateJsons(sizeOfListToGenerate))
                .thenReturn(airportsList);

        //when
        mockMvc.perform(get("/generate/json/" + sizeOfListToGenerate)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]._type", equalTo("uTUclIFUdn")))
                .andExpect(jsonPath("$[*]._id", equalTo(List.of(4563496, 47254818, 12636970))))
                .andReturn();


        //then
        verify(dataService).generateJsons(sizeOfListToGenerate);
    }
}