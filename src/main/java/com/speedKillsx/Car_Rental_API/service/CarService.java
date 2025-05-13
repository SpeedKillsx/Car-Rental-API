package com.speedKillsx.Car_Rental_API.service;

import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService( CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car addCar(String matricule, String model, String state){
        if(carRepository.existsById(matricule)){
            log.error("[addCar] Car with matricule {} already exists", matricule);
        }
        log.info("[addCar] Adding car with matricule {}", matricule);
        Car car = new Car(matricule, model, state);
        return carRepository.save(car);
    }
}
