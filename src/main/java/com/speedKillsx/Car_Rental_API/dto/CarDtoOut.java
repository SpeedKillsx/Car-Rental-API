package com.speedKillsx.Car_Rental_API.dto;

import com.speedKillsx.Car_Rental_API.enums.CAR_STATUS;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarDtoOut {
    private String matricule;
    private String model;
    private CAR_STATUS state;
}
