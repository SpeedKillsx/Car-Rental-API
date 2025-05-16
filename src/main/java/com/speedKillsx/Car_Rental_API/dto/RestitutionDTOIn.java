package com.speedKillsx.Car_Rental_API.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestitutionDTOIn {
    private LocalDate dateBegin;
    private LocalDate dateEnd;
    private LocalDate dateRestitution;
    private String carMatricule;
    private int clientId;
}
