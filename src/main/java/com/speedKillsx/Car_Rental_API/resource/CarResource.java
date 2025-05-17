package com.speedKillsx.Car_Rental_API.resource;

import com.speedKillsx.Car_Rental_API.dto.CarDtoIn;
import com.speedKillsx.Car_Rental_API.dto.CarDtoOut;
import com.speedKillsx.Car_Rental_API.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/cars")
@Tag(name = "Car", description = "Car API")
public class CarResource {
    private final CarService carService;

    public CarResource(CarService carService) {
        this.carService = carService;
    }

    @Operation(
            summary = "Add a car to the system",
            description = "Add a car to the system",
            tags = {"Car"},
            operationId = "createCar",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Car created"),
                    @ApiResponse(responseCode = "400", description = "Bad request")
            }
    )
    @PostMapping("/create")
    public ResponseEntity<CarDtoOut> createCar(@RequestBody CarDtoIn carDtoIn) {
        log.info("[createCar] Creating car");
        CarDtoOut car = carService.addCar(carDtoIn);
        if (car == null) {
            log.error("[createCar] Bad request");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("[createCar] Car created {} :", car.getMatricule());
        return new ResponseEntity<>(car, HttpStatus.ACCEPTED);
    }

    @Operation(
            summary = "Get all cars",
            description = "Get all cars",
            tags = {"Car"},
            operationId = "getAllCars",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cars found"),
                    @ApiResponse(responseCode = "404", description = "No cars found")
            }
    )
    @GetMapping("/")
    public ResponseEntity<List<CarDtoOut>> getAllCars() {
        log.info("[getAllCars] Getting all cars");
        if (carService.getAllCars().isEmpty()) {
            log.error("[getAllCars] No cars found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carService.getAllCars(), HttpStatus.OK);
    }
}
