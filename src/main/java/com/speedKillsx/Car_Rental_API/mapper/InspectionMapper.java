package com.speedKillsx.Car_Rental_API.mapper;

import com.speedKillsx.Car_Rental_API.dto.InspectionDtoIn;
import com.speedKillsx.Car_Rental_API.dto.InspectionDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Inspection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InspectionMapper {
    @Mapping(target = "id", ignore = true)
    InspectionDtoIn toInspectionDtoIn(Inspection inspection);
    Inspection toInspection(InspectionDtoIn inspectionDtoIn);

    @Mapping(target = "carMatricule", source = "car.matricule")
    InspectionDtoOut toInspectionDtoOut(Inspection inspection);
    Inspection toInspection(InspectionDtoOut inspectionDtoOut);
}
