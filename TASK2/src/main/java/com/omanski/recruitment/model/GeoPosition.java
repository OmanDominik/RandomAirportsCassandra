package com.omanski.recruitment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class GeoPosition {
    private float latitude;
    private float longitude;

}
