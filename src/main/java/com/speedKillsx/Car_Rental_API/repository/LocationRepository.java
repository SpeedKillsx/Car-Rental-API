package com.speedKillsx.Car_Rental_API.repository;

import com.speedKillsx.Car_Rental_API.dto.LocationDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    List<Location> findLocationsByClient_Email(String clientEmail);
}
