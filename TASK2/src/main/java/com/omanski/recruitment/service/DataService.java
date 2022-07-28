package com.omanski.recruitment.service;

import com.omanski.recruitment.model.Airport;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyTypedMessageFuture;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataService {

    final
    ReplyingKafkaTemplate<String, String, List<Airport>> generatingAirportsTemplate;

    final
    ProducerFactory<String, String> producerFactory;


    public List<Airport> generateJsons(int size) throws ExecutionException, InterruptedException {

        List<Airport> airportList;

        log.info("Requesting {} airports from generating service through kafka", size);
        RequestReplyTypedMessageFuture<String, String, List<Airport>> future2 =
                generatingAirportsTemplate.sendAndReceive(MessageBuilder
                                .withPayload(size)
                                .build(),
                        new ParameterizedTypeReference<>() {
                        });

        airportList = future2.get().getPayload();

        return airportList;
    }

    //_type, _id, name, type, latitude, longitude
    public List<String> getBasicData(int size) throws ExecutionException, InterruptedException {
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

    @SneakyThrows
    public List<String> getSpecifiedData(int size, List<String> params) throws ExecutionException, InterruptedException {
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

    @SneakyThrows
    public List<String> calculateGivenOperations(List<String> params) throws ExecutionException, InterruptedException {
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
