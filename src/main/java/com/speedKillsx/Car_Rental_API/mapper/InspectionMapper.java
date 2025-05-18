package com.speedKillsx.Car_Rental_API.mapper;

import com.speedKillsx.Car_Rental_API.dto.InspectionDtoIn;
import com.speedKillsx.Car_Rental_API.dto.InspectionDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.entity.Inspection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InspectionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "car.matricule", target = "carMatricule")
    InspectionDtoIn toInspectionDtoIn(Inspection inspection);

    // Map from InspectionDtoIn to Inspection and set Car entity from matricule string
    @Mapping(target = "car", source = "carMatricule")
    Inspection toInspection(InspectionDtoIn inspectionDtoIn);

    @Mapping(target = "carMatricule", source = "car.matricule")
    InspectionDtoOut toInspectionDtoOut(Inspection inspection);

    Inspection toInspection(InspectionDtoOut inspectionDtoOut);

    // Custom method to convert matricule String to Car entity
    default Car map(String carMatricule) {
        if (carMatricule == null) {
            return null;
        }
        Car car = new Car();
        car.setMatricule(carMatricule);
        return car;
    }
}

