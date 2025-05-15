package com.speedKillsx.Car_Rental_API.resource;

import com.speedKillsx.Car_Rental_API.dto.InspectionDtoIn;
import com.speedKillsx.Car_Rental_API.dto.InspectionDtoOut;
import com.speedKillsx.Car_Rental_API.mapper.InspectionMapper;
import com.speedKillsx.Car_Rental_API.service.InspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/inspection")
@Slf4j
@Tag(name = "Inspection", description = "Inspection API")
public class InspectionResource {

    private final InspectionService inspectionService;
    private final InspectionMapper inspectionMapper;

    public InspectionResource(InspectionService inspectionService, InspectionMapper inspectionMapper) {
        this.inspectionService = inspectionService;
        this.inspectionMapper = inspectionMapper;
    }

    @Operation(
            summary = "Create a new Inspection",
            description = "Create a new Inspection for a specific car",
            tags = {"Inspection"},
            operationId = "createInspection",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Inspection created"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<InspectionDtoOut> createInspection(@RequestBody InspectionDtoIn inspectionDtoIn) {
        log.info("[createInspection] Creating inspection");
        InspectionDtoOut inspectionDtoOut = inspectionService.addInspection(inspectionDtoIn);
        if (inspectionDtoOut == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(inspectionDtoOut, HttpStatus.CREATED);
    }


    @Operation(summary = "Obligate client",
    responses = {
            @ApiResponse(responseCode = "200", description = "Client obligated"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping("/obligate")
    public ResponseEntity<InspectionDtoOut> obligate(@RequestParam String matricule){
        log.info("[obligate] Obligating client");
        InspectionDtoOut inspectionDtoOut = inspectionService.obligateInspection(matricule);
        if (inspectionDtoOut == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(inspectionDtoOut, HttpStatus.OK);
    }

}
