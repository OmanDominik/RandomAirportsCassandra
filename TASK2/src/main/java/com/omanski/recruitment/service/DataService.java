package com.omanski.recruitment.service;

import com.omanski.recruitment.model.Airport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    Logger log = LoggerFactory.getLogger(DataService.class);
    @Autowired
    private RestTemplate restTemplate;
    @Value("${generating.url}")
    private String url;

    public String buildGeneratingUri(int size) {
        return url + "generate/json/" + size;
    }

    public List<Airport> generateJsons(int size) {

        String uri = buildGeneratingUri(size);
        log.info("Fetching: " + uri);
        ResponseEntity<Airport[]> response = restTemplate.getForEntity(uri, Airport[].class);

        Airport[] generatedList = response.getBody();

        return List.of(generatedList);
    }

    //_type, _id, name, type, latitude, longitude
    public List<String> getBasicData(int size) {
        List<Airport> airportsList = this.generateJsons(size);

        List<String> result = new ArrayList<>();
        for (Airport airport : airportsList) {
            List<String> line = new ArrayList<>();

            line.add(airport.get_type());
            line.add(String.valueOf(airport.get_id()));
            line.add(airport.getName());
            line.add(airport.getType());
            line.add(String.valueOf(airport.getGeo_position().getLatitude()));
            line.add(String.valueOf(airport.getGeo_position().getLongitude()));

            result.add(String.join(",", line));
        }
        return result;
    }

    public List<String> getSpecifiedData(int size, List<String> params) throws IllegalAccessException {
        List<Airport> airportsList = this.generateJsons(size);
        List<String> result = new ArrayList<>();

        for (Airport airport : airportsList) {
            List<String> line = new ArrayList<>();
            for (String param : params) {
                if (Airport.fieldsMap.containsKey(param)) {
                    line.add(String.valueOf(Airport.fieldsMap.get(param).get(airport)));
                } else if (Airport.geoFieldsMap.containsKey(param)) {
                    line.add(String.valueOf(Airport.geoFieldsMap.get(param).get(airport.getGeo_position())));
                }
            }
            result.add(String.join(",", line));
        }
        return result;
    }

    public List<String> calculateGivenOperations(List<String> params) throws IllegalAccessException {
        List<String> results = new ArrayList<>();

        Airport airport = this.generateJsons(1).get(0);

        for (String operation : params) {
            Double result;

            for (String parameterName : Airport.fieldsMap.keySet()) {
                if (operation.contains(parameterName)) {
                    operation = operation.replace(parameterName, String.valueOf(Airport.fieldsMap.get(parameterName).get(airport)));
                }
            }
            for (String parameterName : Airport.geoFieldsMap.keySet()) {
                if (operation.contains(parameterName)) {
                    operation = operation.replace(parameterName, String.valueOf(Airport.geoFieldsMap.get(parameterName).get(airport.getGeo_position())));
                }
            }

            try {
                ExpressionParser parser = new SpelExpressionParser();
                result = parser.parseExpression(operation).getValue(Double.class);

                results.add(String.valueOf(result));
            } catch (SpelEvaluationException | SpelParseException see) {
                results.add("Illegal operation called: " + operation);
            }

        }

        return results;
    }
}
