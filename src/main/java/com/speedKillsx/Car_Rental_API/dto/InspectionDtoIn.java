package com.speedKillsx.Car_Rental_API.dto;

import com.speedKillsx.Car_Rental_API.enums.INSPECTION_STATUS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionDtoIn {
    private int id;
    private INSPECTION_STATUS stateInspection;
    private LocalDate dateInspection;
    private String carMatricule;
}
