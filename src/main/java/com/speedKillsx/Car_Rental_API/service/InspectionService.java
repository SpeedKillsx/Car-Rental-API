package com.speedKillsx.Car_Rental_API.service;

import com.speedKillsx.Car_Rental_API.dto.InspectionDtoIn;
import com.speedKillsx.Car_Rental_API.dto.InspectionDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.entity.Inspection;
import com.speedKillsx.Car_Rental_API.entity.Location;
import com.speedKillsx.Car_Rental_API.enums.INSPECTION_STATUS;
import com.speedKillsx.Car_Rental_API.mapper.InspectionMapper;
import com.speedKillsx.Car_Rental_API.repository.CarRepository;
import com.speedKillsx.Car_Rental_API.repository.InspectionRepository;
import com.speedKillsx.Car_Rental_API.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    private final InspectionMapper inspectionMapper;
    private final CarRepository carRepository;
    private final LocationRepository locationRepository;

    public InspectionService(InspectionRepository inspectionRepository,
                             InspectionMapper inspectionMapper, CarRepository carRepository, LocationRepository locationRepository) {
        this.inspectionRepository = inspectionRepository;
        this.inspectionMapper = inspectionMapper;
        this.carRepository = carRepository;
        this.locationRepository = locationRepository;
    }

    public InspectionDtoOut addInspection(InspectionDtoIn inspectionDtoIn) {
        log.info("[addInspection] Adding inspection for car with matricule {}...", inspectionDtoIn.getCarMatricule());
        Inspection inspection = inspectionMapper.toInspection(inspectionDtoIn);
        log.info("[addInspection] Inspection created with id {} for car {} ", inspection.getId(), inspection.getCar().getMatricule());
        String carMatricule = inspection.getCar().getMatricule();

        Car carInspected = carRepository.findByMatricule(carMatricule);
        if(carInspected == null){
            log.error("[addInspection] Car with matricule {} not found", inspection.getCar().getMatricule());
            return null;
        }
        Inspection saveInspection =  inspectionRepository.save(inspection);
        carInspected.getInspections().add(saveInspection);
        carRepository.save(carInspected);

        return inspectionMapper.toInspectionDtoOut(saveInspection);
    }

    public boolean isCarWithinAllowedRentalPeriod(String carMatricule) {
        Car car = carRepository.findById(carMatricule)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        List<Location> rentals = locationRepository.findAllByCarOrderByDateBeginAsc(car);
        List<Inspection> inspections = inspectionRepository.findAllByCarOrderByDateInspectionAsc(car);

        long consecutiveDays = 0;

        for (Location rental : rentals) {
            LocalDate start = rental.getDateBegin();
            LocalDate end = rental.getDateEnd() != null ? rental.getDateEnd() : LocalDate.now();

            boolean hasInspectionDuringRental = inspections.stream()
                    .anyMatch(insp -> !insp.getDateInspection().isBefore(start) && !insp.getDateInspection().isAfter(end));

            if (hasInspectionDuringRental) {
                consecutiveDays = 0;
            } else {
                consecutiveDays += ChronoUnit.DAYS.between(start, end);
                if (consecutiveDays > 60) {
                    return false;
                }
            }
        }

        return true;
    }


    public InspectionDtoOut obligateInspection(String carMatricule){
        Car car = carRepository.findByMatricule(carMatricule);
        InspectionDtoOut inspection = InspectionDtoOut.builder()
                .stateInspection(INSPECTION_STATUS.GENERAL_INSPECTION)
                .dateInspection(LocalDate.now())
                .build();
        car.getInspections().add(inspectionMapper.toInspection(inspection));
        carRepository.save(car);
        return inspection;
    }

}
