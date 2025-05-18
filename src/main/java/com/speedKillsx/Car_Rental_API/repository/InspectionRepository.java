package com.speedKillsx.Car_Rental_API.repository;

import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.entity.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Integer> {
    Inspection findByCar_Matricule(String carMatricule);

    List<Inspection> findAllByCar_Matricule(String carMatricule );

    List<Inspection> findAllByCarOrderByDateInspectionAsc(Car car);

    @Query("SELECT c FROM Car c JOIN c.inspections i WHERE i.dateInspection <= :targetDate AND i.stateInspection = 'NOT_PASSED'")
    List<Car> findCarsWithFailedInspection(@Param("targetDate") LocalDate targetDate);



}
