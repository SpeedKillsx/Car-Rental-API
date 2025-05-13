package com.speedKillsx.Car_Rental_API.mapper;

import com.speedKillsx.Car_Rental_API.dto.CarDtoIn;
import com.speedKillsx.Car_Rental_API.dto.CarDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Car;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarDtoOut toCarDtoOut(Car car);

    Car toCar(CarDtoOut carDtoOut);

    CarDtoIn toCarDtoIn(Car car);
    Car toCar(CarDtoIn carDtoIn);

    List<CarDtoOut> toCarDtoOutList(List<Car> cars);
    List<Car> toCarList(List<CarDtoOut> carsDtoOut);
}
