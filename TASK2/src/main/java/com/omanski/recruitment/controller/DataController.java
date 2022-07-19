package com.omanski.recruitment.controller;

import com.omanski.recruitment.service.DataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name="Airports data handler")
public class DataController {

    final
    DataService dataService;

    @Operation(
            summary = "Returns list of given size containing basic data of randomly generated airports",
            description = "Returns _type, _id, name, type, latitude, longitude of generated airports",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = String.class))
                            )
                    ),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    @GetMapping("/basicData/{size}")
    public List<String> getBasicData(
            @PathVariable("size")
            @Parameter(description="number of airports to get data from")
            int size
    ){
        return  dataService.getBasicData(size);
    }

    @Operation(
            summary = "Returns list of given size containing specified data of randomly generated airports",
            description = "Returns the properties given in the parameters of the generated airports." +
            "For example: '_id,latitude,longitude’",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
                    ),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    @GetMapping("/specifiedData/{size}")
    public List<String> getSpecifiedData(
            @PathVariable("size")
            @Parameter(description = "number of airports to generate data from")
            int size,
            @RequestParam
            @Parameter(description = "properties of the generated airports to be returned (E.g. '_id,latitude,longitude’)")
            List<String> params
    ) throws IllegalAccessException {
        return  dataService.getSpecifiedData(size, params);
    }

    @Operation(
            summary = "Mathematical operations on the properties of an airport",
            description = "Calculates simple mathematical operations given in request parameters based on data in " +
                    "randomly generated airport. For example: ‘latitude*longitude,T(Math).sqrt(location_id)'",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))
                    ),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    @GetMapping("/mathematicalOperations")
    public List<String> calculateOperations(
            @RequestParam
            @Parameter(description = "operations to calculate (E.g. ‘latitude*longitude,T(Math).sqrt(location_id)')")
            List<String> operations
    ) throws IllegalAccessException {
        return  dataService.calculateGivenOperations(operations);
    }

}