package com.speedKillsx.Car_Rental_API.repository;

import com.speedKillsx.Car_Rental_API.dto.RestitutionDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.entity.Location;
import com.speedKillsx.Car_Rental_API.enums.LOCATION_STATE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findLocationsByClient_Email(String clientEmail);


    List<Location> findAllByCarOrderByDateBeginAsc(Car car);

    @Query("SELECT new com.speedKillsx.Car_Rental_API.dto.RestitutionDtoOut(l.id,l.dateBegin, l.dateEnd, null,l.client.id, l.car.matricule) from Location l" +
            " where l.dateBegin =:dateBegin and l.dateEnd =:dateEnd AND " +
            "l.car.matricule =:matricule AND " +
            "l.locationState = 'ACTIVE'")
    RestitutionDtoOut findRentedCar(@Param("dateBegin") LocalDate dateBegin,
                                    @Param("dateEnd") LocalDate dateEnd,
                                    @Param("matricule") String matricule
    );
}
