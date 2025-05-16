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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
@Slf4j
@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;
    @Mock
    private InspectionRepository inspectionRepository;
    @Mock
    private InspectionService inspectionService;


    @InjectMocks
    private CarService carService;

    @BeforeEach
    public void setUp() {
    }


    @Test
    public void findAllCars_success() {
        // Arrange
        Car car = Car.builder()
                .matricule("123456")
                .model("model")
                .distanceTraveled(0L)
                .state(CAR_STATUS.AVAILABLE)
                .build();

        CarDtoOut dto = CarDtoOut.builder()
                .matricule("123456")
                .model("model")
                .distanceTraveled(0L)
                .state(CAR_STATUS.AVAILABLE)
                .build();

        when(carRepository.findAll()).thenReturn(List.of(car));
        when(carMapper.toCarDtoOutList(List.of(car))).thenReturn(List.of(dto));


        // Act
        List<CarDtoOut> result = carService.getAllCars();

        // Assert
        assertNotNull(result);
        assertThat(result).hasSize(1);
        assertEquals(result.get(0), dto);
        assertEquals(carMapper.toCarDtoOutList(List.of(car)), List.of(dto));
        assertEquals("123456", result.get(0).getMatricule());

    }

    @Test
    public void addCar_success() {
        Car car = Car.builder()
                .matricule("123456")
                .model("model")
                .distanceTraveled(0L)
                .state(CAR_STATUS.AVAILABLE)
                .build();


        CarDtoIn carDtoIn = CarDtoIn.builder()
                .matricule("123456")
                .model("model")
                .distanceTraveled(0L)
                .build();

        CarDtoOut dto = CarDtoOut.builder()
                .matricule("123456")
                .model("model")
                .distanceTraveled(0L)
                .state(CAR_STATUS.AVAILABLE)
                .build();

        when(carRepository.findByMatricule("123456")).thenReturn(car);
        when(carMapper.toCar(carDtoIn)).thenReturn(car);
        when(carMapper.toCarDtoOut(car)).thenReturn(dto);
        when(carRepository.save(car)).thenReturn(car);


        CarDtoOut result = carService.addCar(carDtoIn);

        assertNotNull(result);
        assertEquals(result, dto);


    }

    @Test
    public void isCarRentable_success() {
        Car car = Car.builder()
                .matricule("123456")
                .model("model")
                .distanceTraveled(0L)
                .state(CAR_STATUS.AVAILABLE)
                .build();

        Car car2 = Car.builder()
                .matricule("12369")
                .model("model")
                .distanceTraveled(1500000)
                .inspections(List.of(
                                Inspection.builder()
                                        .stateInspection(INSPECTION_STATUS.NOT_PASSED)
                                        .dateInspection(LocalDate.now())
                                        .build()
                        )
                )
                .state(CAR_STATUS.INACTIVE)
                .build();

        List<Inspection> inspections = List.of(Inspection.builder()
                .car(car)
                .dateInspection(LocalDate.now())
                .build()
        );

        when(carRepository.findByMatricule(car.getMatricule())).thenReturn(car);
        boolean result = carService.isCarRentable(car.getMatricule());
        boolean result2 = carService.isCarRentable(car2.getMatricule());
        assertEquals(result, true);
        assertEquals(result2, false);
        assertEquals(car.getState(), CAR_STATUS.AVAILABLE);
        assertEquals(car2.getState(), CAR_STATUS.INACTIVE);

    }

    @Test
    public void updateCarState_success() {
        CarDtoIn carDtoIn = CarDtoIn.builder()
                .matricule("123456")
                .model("model")
                .distanceTraveled(0L)
                .build();
        Car car = Car.builder()
                .matricule("123456")
                .model("model")
                .distanceTraveled(0L)
                .state(CAR_STATUS.AVAILABLE)
                .build();

        CarDtoOut carDtoOut = CarDtoOut.builder()
                .matricule("123456")
                .model("model")
                .distanceTraveled(0L)
                .state(CAR_STATUS.ON_LOCATION)
                .build();

        when(carRepository.findByMatricule(carDtoIn.getMatricule())).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toCarDtoOut(car)).thenReturn(carDtoOut);

        CarDtoOut result = carService.updateCarState(car.getMatricule(), CAR_STATUS.ON_LOCATION);
        log.info("[updateCarState] Result: {}", result.getState());
        assertNotNull(result);
        assertEquals(CAR_STATUS.ON_LOCATION, result.getState());

    }

}
