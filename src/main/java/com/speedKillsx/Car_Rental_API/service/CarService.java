package com.speedKillsx.Car_Rental_API.service;

import com.speedKillsx.Car_Rental_API.dto.CarDtoIn;
import com.speedKillsx.Car_Rental_API.dto.CarDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.entity.Inspection;
import com.speedKillsx.Car_Rental_API.enums.CAR_STATUS;
import com.speedKillsx.Car_Rental_API.enums.INSPECTION_STATUS;
import com.speedKillsx.Car_Rental_API.mapper.CarMapper;
import com.speedKillsx.Car_Rental_API.repository.CarRepository;
import com.speedKillsx.Car_Rental_API.repository.InspectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.speedKillsx.Car_Rental_API.enums.CAR_STATUS.AVAILABLE;

@Slf4j
@Service
public class CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final InspectionRepository inspectionRepository;
    private final InspectionService inspectionService;
    public CarService(CarRepository carRepository, CarMapper carMapper,
                      InspectionRepository inspectionRepository,
                      InspectionService inspectionService) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.inspectionRepository = inspectionRepository;
        this.inspectionService = inspectionService;
    }

    public CarDtoOut addCar(CarDtoIn carDtoIn) {
        if (carRepository.existsById(carDtoIn.getMatricule())) {
            log.error("[addCar] Car with matricule {} already exists", carDtoIn.getMatricule());
            return null;
        }
        log.info("[addCar] Adding car with matricule {}", carDtoIn.getMatricule());
        Car car = carMapper.toCar(carDtoIn);
        car.setState(AVAILABLE);
        carRepository.save(car);
        return carMapper.toCarDtoOut(car);
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

        /*CHECK INSPECTION*/
        if (car.getState() == CAR_STATUS.MAINTENANCE) return false;
        if (car.getDistanceTraveled()> 150000L){
            log.warn("[isCarRentable] Car has more than 150km traveled");
            log.info("[isCarRentable] Check if car is under maintenance");
            List<Inspection> inspections = inspectionRepository.findAllByCar_Matricule(car.getMatricule());
            for (Inspection inspection : inspections) {
                if(inspection.getStateInspection() == INSPECTION_STATUS.TECHNICAL_INSPECTION) return true;
            }
            log.warn("[isCarRentable] The car traveled more than 150km without a technical inspection");
            return false;
        }

        if(inspectionService.isCarWithinAllowedRentalPeriod(matricule)){
            return true;
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
