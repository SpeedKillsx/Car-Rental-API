package com.speedKillsx.Car_Rental_API.dto;

import com.speedKillsx.Car_Rental_API.enums.LOCATION_STATE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDtoOut {
    private int id;
    private LocalDate dateBegin;
    private LocalDate dateEnd;
    private BigDecimal amount;
    private LOCATION_STATE locationState;
    private String carMatricule;
    private int clientId;
}
