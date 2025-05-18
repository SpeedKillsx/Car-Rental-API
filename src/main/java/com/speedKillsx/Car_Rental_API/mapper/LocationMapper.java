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
    LocationDtoIn toLocationDtoIn(Location location);
    Location toLocation(LocationDtoIn locationDtoIn);

    LocationDtoOut toLocationDtoOut(Location location);

    List<LocationDtoOut> toLocationDtoOutList(List<Location> locations);

}
