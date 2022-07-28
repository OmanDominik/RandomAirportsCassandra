package com.omanski.recruitment.service;

import com.omanski.recruitment.model.Airport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class DataService {

    final AirportService airportService;

    public List<Airport> generateJsons(int size) {

        List<Airport> generatedList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Airport randomAirport = Airport.getRandomInstance();
            generatedList.add(randomAirport);
            airportService.save(randomAirport);
        }

        return generatedList;
    }

    @KafkaListener(topics = "kRequests", groupId = "repliesGroup")
    @SendTo
    public Message<?> messageReturn(String size) {
        log.info("Responding with {} generated airports through kafka", size);
        return MessageBuilder.withPayload(generateJsons(Integer.parseInt(size)))
                .setHeader(KafkaHeaders.REPLY_TOPIC, "kReplies".getBytes())
                .build();
    }

}
