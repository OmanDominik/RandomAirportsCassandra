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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    void shouldReturnBasicData() throws Exception {
        //given
        int sizeOfListToGenerate = 2;
        List<String> basicData = Arrays.asList("a,1,b,d,3,4", "g,2,i,j,5,6");
        Mockito
                .when(dataService.getBasicData(sizeOfListToGenerate))
                .thenReturn(basicData);

        //when
        MvcResult response = mockMvc.perform(get("/basicData/" + sizeOfListToGenerate))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        //then
        verify(dataService).getBasicData(sizeOfListToGenerate);
        assertThat(response.getResponse().getContentAsString().contains("a,1,b,d,3,4"));
    }

    @Test
    void shouldReturnSpecifiedData() throws Exception {
        //given
        int sizeOfListToGenerate = 2;
        List<String> specifiedData = Arrays.asList("a,2121,b,d,3,abc", "g,253,i,j,5,def");
        List<String> params = Arrays.asList("_type", "_id", "name", "fullName", "distance","type");
        Mockito
                .when(dataService.getSpecifiedData(sizeOfListToGenerate, params))
                .thenReturn(specifiedData);

        //when
        MvcResult response = mockMvc.perform(get("/specifiedData/" + sizeOfListToGenerate + "?params=" + String.join(",", params)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        //then
        verify(dataService).getSpecifiedData(sizeOfListToGenerate, params);
        assertThat(response.getResponse().getContentAsString().contains("a,2121,b,d,3,abc"));
        assertThat(response.getResponse().getContentAsString().contains("g,253,i,j,5,def"));
    }

    @Test
    void calculateOperations() throws Exception {
        //given
        int sizeOfListToGenerate = 1;
        List<Airport> airport = Arrays.asList(
                new Airport("uTUclIFUdn", 15, 106, "name", "yvjPMgmdYKSiEJUTKFSr",
                        "EZP", "AwOFMFUW","nIkaLW",
                        new GeoPosition(-11.0f, -71.2f),
                        64, false, "UX", false, 2614)
        );
        List<String> params = Arrays.asList("latitude*longitude", "T(Math).sqrt(location_id)", "distance-name", "key-(-_id)");
        Mockito
                .when(dataService.calculateGivenOperations(params))
                .thenReturn(Arrays.asList("783.2","8","Illegal operation called: 2614-name","121"));

        //when
        MvcResult response = mockMvc.perform(get("/mathematicalOperations/" + "?operations=" + String.join(",", params)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        //then
        verify(dataService).calculateGivenOperations(params);
        assertThat(response.getResponse().getContentAsString().contains("783.2"));
        assertThat(response.getResponse().getContentAsString().contains("Illegal"));
    }
}