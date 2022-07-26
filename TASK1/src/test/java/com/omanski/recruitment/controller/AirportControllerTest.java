package com.omanski.recruitment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.model.GeoPosition;
import com.omanski.recruitment.service.AirportService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirportController.class)
@ExtendWith(MockitoExtension.class)
class AirportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AirportService airportService;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldReturnAllAirports() throws Exception {
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
                .when(airportService.getAirports())
                .thenReturn(airportsList);

        //when
        mockMvc.perform(get("/airports")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[*]._id", equalTo(List.of(4563496, 47254818, 12636970))))
                .andExpect(jsonPath("$", hasSize(3)));

        //then
        verify(airportService).getAirports();

    }

    @Test
    void shouldAddAirport() throws Exception {
        //given
        Airport airport = new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                "EZP", "AwOFMFUW", "nIkaLW",
                new GeoPosition(-11.26171f, -71.221405f),
                723086, false, "EB", false, 6988);

        Mockito
                .when(airportService.save(airport))
                .thenReturn(airport);

        //when
        mockMvc.perform(post("/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(airport))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$._id", equalTo(4563496)));
        
        //then
        verify(airportService).save(airport);
    }

    @Test
    void shouldNotAddAirport() throws Exception {
        //given
        Airport airport = new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                "EZP", "AwOFMFUW", "nIkaLW",
                new GeoPosition(-11.26171f, -71.221405f),
                723086, false, "EB", false, 6988);

        Mockito
                .when(airportService.save(airport))
                .thenThrow(new IllegalArgumentException());

        //when
        MvcResult result = mockMvc.perform(post("/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(airport))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andDo(print())
                .andReturn();

        //then
        assertThat(result.getResponse().getContentAsString().isEmpty());
        verify(airportService).save(airport);
    }

}