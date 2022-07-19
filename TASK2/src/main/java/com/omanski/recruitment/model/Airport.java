package com.omanski.recruitment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.lang.reflect.Field;
import java.util.*;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Airport {

    public static HashMap<String, Field> fieldsMap;
    public static HashMap<String, Field> geoFieldsMap;

    public static List<String> classParams;

    static {
        fieldsMap = new HashMap<>();
        geoFieldsMap = new HashMap<>();
        classParams = new ArrayList<>();
        Field[] fields = Airport.class.getDeclaredFields();
        Field[] geoFields = GeoPosition.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            fieldsMap.put(field.getName(), field);
        }

        for (Field field : geoFields) {
            field.setAccessible(true);
            geoFieldsMap.put(field.getName(), field);
        }

    }

    private String _type;
    private int _id;
    private int key;
    private String name;
    private String fullName;
    private String iata_airport_code;
    private String type;
    private String country;
    private GeoPosition geo_position;
    private int location_id;
    private boolean inEurope;
    private String countryCode;
    private boolean coreCountry;
    private int distance;

    public static Airport getRandomInstance(){
        Random random = new Random();
        GeoPosition geo_position;

        String _type = RandomStringUtils.randomAlphabetic(10);
        int _id = getRandomInt(0, 100000000);
        int key = getRandomInt(0, 10000);
        String name = RandomStringUtils.randomAlphabetic(7);
        String fullName = RandomStringUtils.randomAlphabetic(20);
        String iata_airport_code = RandomStringUtils.randomAlphabetic(3).toUpperCase(Locale.ROOT);
        String type = RandomStringUtils.randomAlphabetic(8);
        String country = RandomStringUtils.randomAlphabetic(6);
        float latitude = getRandomFloat(-90, 90);
        float longitude = getRandomFloat(-180, 180);
        int location_id = getRandomInt(0, 1000000);
        boolean inEurope = random.nextBoolean();
        String countryCode = RandomStringUtils.randomAlphabetic(2).toUpperCase(Locale.ROOT);
        boolean coreCountry = random.nextBoolean();
        int distance = getRandomInt(0, 10000);

        geo_position = new GeoPosition(latitude, longitude);

        return new Airport(_type, _id, key, name, fullName, iata_airport_code, type, country, geo_position, location_id, inEurope, countryCode, coreCountry, distance);
    }

    private static int getRandomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private static float getRandomFloat(float min, float max) {
        return (float) ((Math.random() * (max - min)) + min);
    }

}
