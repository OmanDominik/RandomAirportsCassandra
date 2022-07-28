package com.omanski.recruitment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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


}
