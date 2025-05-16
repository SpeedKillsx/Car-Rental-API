package com.speedKillsx.Car_Rental_API.dto;

import com.speedKillsx.Car_Rental_API.enums.CAR_STATUS;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CarDtoIn {
    private String matricule;
    private String model;
    private long distanceTraveled;
}
