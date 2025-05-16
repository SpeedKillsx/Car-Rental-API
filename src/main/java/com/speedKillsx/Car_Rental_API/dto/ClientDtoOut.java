package com.speedKillsx.Car_Rental_API.dto;

import com.speedKillsx.Car_Rental_API.enums.CLIENT_STATUS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDtoOut {
    private String firstName;
    private String lastName;
    private String email;
    private float debts;
    private CLIENT_STATUS stateClient;
}
