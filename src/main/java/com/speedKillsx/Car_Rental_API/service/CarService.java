package com.speedKillsx.Car_Rental_API.service;

import com.speedKillsx.Car_Rental_API.dto.CarDtoIn;
import com.speedKillsx.Car_Rental_API.dto.CarDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.enums.CAR_STATUS;
import com.speedKillsx.Car_Rental_API.mapper.CarMapper;
import com.speedKillsx.Car_Rental_API.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.speedKillsx.Car_Rental_API.enums.CAR_STATUS.AVAILABLE;

@Slf4j
@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    public CarDtoOut addCar(CarDtoIn carDtoIn) {
        if (carRepository.existsById(carDtoIn.getMatricule())) {
            log.error("[addCar] Car with matricule {} already exists", carDtoIn.getMatricule());
        }
        log.info("[addCar] Adding car with matricule {}", carDtoIn.getMatricule());
        CarDtoOut carDtoOut = CarDtoOut.builder()
                .matricule(carDtoIn.getMatricule())
                .model(carDtoIn.getModel())
                .state(AVAILABLE)
                .build();
        carRepository.save(carMapper.toCar(carDtoOut));
        return carDtoOut;
    }

    public List<CarDtoOut> getAllCars() {
        log.info("[getAllCars] Getting all cars");
        if (carRepository.findAll().isEmpty()) {
            log.error("[getAllCars] No cars found");
            return List.of();
        }
        return carMapper.toCarDtoOutList(carRepository.findAll());
    }

    public boolean isCarRentable(String matricule) {
        Car car = carRepository.findByMatricule(matricule);
        if (car == null) return false;
        switch (car.getState()) {
            case AVAILABLE -> {
                return true;
            }
            case ON_LOCATION -> log.warn("[isCarRentable] Car is on location");
            case MAINTENANCE -> log.warn("[isCaerRentable] Car is under maintenance");
            case INACTIVE -> log.warn("[isCarRentable] Car is inactive");
            default -> {
                return false;
            }

        }

        return false;
    }

    public CarDtoOut updateCarState(String matricule, CAR_STATUS status){
        Car car = carRepository.findByMatricule(matricule);
        if(car == null) return null;
        car.setState(status);
        carRepository.save(car);
        return carMapper.toCarDtoOut(car);
    }
}
