package com.speedKillsx.Car_Rental_API.repository;

import com.speedKillsx.Car_Rental_API.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {
    Car findByMatricule(String matricule);
}
