package com.speedKillsx.Car_Rental_API.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
@Entity
@Slf4j
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    private String matricule;

    private String model;
    private String state;
}
