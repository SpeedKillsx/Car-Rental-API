package com.speedKillsx.Car_Rental_API.dto;

import com.speedKillsx.Car_Rental_API.enums.CLIENT_STATUS;
import com.speedKillsx.Car_Rental_API.enums.LOCATION_STATE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestitutionDtoOut {
    private int id;
    private LocalDate dateBegin;
    private LocalDate dateEnd;
    private LocalDate dateRestitution;
    private int clientId;
    private String carMatricule;
    private LOCATION_STATE locationState;
    private CLIENT_STATUS clientStatus;

}
