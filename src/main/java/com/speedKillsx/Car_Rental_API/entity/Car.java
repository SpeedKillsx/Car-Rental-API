package com.speedKillsx.Car_Rental_API.entity;

import com.speedKillsx.Car_Rental_API.enums.CAR_STATUS;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id
    private String matricule;

    private String model;
    @Enumerated(EnumType.STRING)
    private CAR_STATUS state;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> location;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Inspection> inspections;
}
