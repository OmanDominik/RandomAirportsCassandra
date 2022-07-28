package com.omanski.recruitment.controller;

import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.service.DataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Airports generator")
@Slf4j
public class DataController {

    final
    DataService dataService;

    @Operation(
            summary = "Random airports list generator",
            description = "Generates a list of the given size containing airports with randomly generated properties.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Airport.class)))
                    ),
                    @ApiResponse(description = "One of generated airports already exists", responseCode = "409", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    @GetMapping("/generate/json/{size}")
    public @ResponseBody ResponseEntity<List<Airport>> generateList(
            @PathVariable("size")
            @Parameter(description = "number of airports to generate")
            int size
    ) {
        try {
            return new ResponseEntity<>(dataService.generateJsons(size), HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            log.error("One of generated airports had id that already exists", exception);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

    }

}
