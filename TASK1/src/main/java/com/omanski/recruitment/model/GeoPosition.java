package com.omanski.recruitment.model;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
@UserDefinedType(value = "geo_position")
public class GeoPosition implements Serializable {
    private float latitude;
    private float longitude;

}
