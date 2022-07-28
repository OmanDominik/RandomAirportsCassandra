package com.omanski.recruitment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@UserDefinedType(value = "geo_position")
public class GeoPosition implements Serializable {
    private float latitude;
    private float longitude;

}
