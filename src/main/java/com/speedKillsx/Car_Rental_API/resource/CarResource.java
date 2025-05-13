package com.speedKillsx.Car_Rental_API.resource;

import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("api/v1/cars")
public class CarResource {
    private final CarService carService;

    public CarResource(CarService carService) {
        this.carService = carService;
    }
    @PostMapping("/create")
    public void createCar(@RequestBody Car body) {
      log.info("[createCar] Creating car");
      carService.addCar(body.getMatricule(), body.getModel(), body.getState());
    }
}
