package com.speedKillsx.Car_Rental_API.mapper;

import com.speedKillsx.Car_Rental_API.dto.LocationDtoIn;
import com.speedKillsx.Car_Rental_API.dto.LocationDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "car.matricule", target = "carMatricule")
    @Mapping(source = "client.id", target = "clientId")
    LocationDtoIn toLocationDtoIn(Location location);
    Location toLocation(LocationDtoIn locationDtoIn);

    @Mapping(target = "carMatricule", source = "car.matricule")
    @Mapping(target = "clientId", source = "client.id" )
    LocationDtoOut toLocationDtoOut(Location location);

    List<LocationDtoOut> toLocationDtoOutList(List<Location> locations);

}
