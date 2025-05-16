package com.speedKillsx.Car_Rental_API.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDtoIn {
    private String firstName;
    private String lastName;
    private String email;
    private float debts;

}
