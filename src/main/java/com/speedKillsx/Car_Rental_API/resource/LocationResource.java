package com.speedKillsx.Car_Rental_API.resource;

import com.speedKillsx.Car_Rental_API.dto.LocationDtoIn;
import com.speedKillsx.Car_Rental_API.dto.LocationDtoOut;
import com.speedKillsx.Car_Rental_API.mapper.LocationMapper;
import com.speedKillsx.Car_Rental_API.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/location")
@Tag(name = "Location", description = "Location API")
public class LocationResource {

    private final LocationMapper locationMapper;
    private final LocationService locationService;
    @Autowired
    public LocationResource(LocationMapper locationMapper, LocationService locationService) {
        this.locationMapper = locationMapper;
        this.locationService = locationService;
    }
    @PostMapping("/create")
    public ResponseEntity<LocationDtoOut> createLocation(@RequestBody LocationDtoIn locationDtoIn){
        if(locationDtoIn == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(locationService.addLocation(locationDtoIn), HttpStatus.CREATED);
    }

    @Operation(summary = "Get client locations")
    @GetMapping("/{email}")
    public ResponseEntity<List<LocationDtoOut>> getClientLocations(@PathVariable String email){
        if(email == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(locationService.getAllLocations(email), HttpStatus.FOUND);
    }
}
