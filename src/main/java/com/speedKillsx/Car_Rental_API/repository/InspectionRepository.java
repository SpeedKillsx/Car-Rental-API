package com.speedKillsx.Car_Rental_API.repository;

import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.entity.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Integer> {
    Inspection findByCar_Matricule(String carMatricule);

    List<Inspection> findAllByCar_Matricule(String carMatricule );

    List<Inspection> findAllByCarOrderByDateInspectionAsc(Car car);
}
