package com.speedKillsx.Car_Rental_API.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDtoOut {
    private int id;
    private LocalDate dateBegin;
    private LocalDate dateEnd;
    private String locationState;
    private String carMatricule;
    private int clientId;
}
