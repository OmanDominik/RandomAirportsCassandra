package com.omanski.recruitment.configuration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omanski.recruitment.model.Airport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.*;

@Configuration
@EnableKafka
@Slf4j
@Getter
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String server;

    @Bean
    public ConsumerFactory<String, List<Airport>> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getServer());
        ObjectMapper om = new ObjectMapper();
        JavaType type = om.getTypeFactory().constructParametricType(List.class, Airport.class);
        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<List<Airport>>(type, om, false));
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                getServer());
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ?> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean
    ReplyingKafkaTemplate<String, String, List<Airport>> generatingAirportsTemplate(
            ProducerFactory<String, String> pf,
            ConcurrentKafkaListenerContainerFactory<String, List<Airport>> factory
    ) {

        ConcurrentMessageListenerContainer<String, List<Airport>> replyContainer =
                factory.createContainer("replies");

        replyContainer.getContainerProperties().setGroupId(UUID.randomUUID().toString());

        Properties props = new Properties();
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        replyContainer.getContainerProperties().setKafkaConsumerProperties(props);

        ReplyingKafkaTemplate<String, String, List<Airport>> template =
                new ReplyingKafkaTemplate<>(pf, replyContainer);

        template.setSharedReplyTopic(true);
        template.setMessageConverter(new StringJsonMessageConverter());
        template.setDefaultTopic("kRequests");

        return template;
    }

    @Bean
    public NewTopic receivingTopic() {
        return new NewTopic("kReplies", 10, (short) 1);
    }

    @Bean
    public NewTopic generatingTopic() {
        return new NewTopic("kRequests", 10, (short) 1);
    }


}
