package com.speedKillsx.Car_Rental_API.repository;

import com.speedKillsx.Car_Rental_API.dto.ClientLocationDto;
import com.speedKillsx.Car_Rental_API.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client getClientByEmail(String email);

    @Query("SELECT new com.speedKillsx.Car_Rental_API.dto.ClientLocationDto( c.id, count(l.locationState) ) from Client c JOIN Location l on c.id = l.client.id AND l.locationState = 'ACTIVE' AND " +
            "c.email =:email GROUP BY c.id")
    Optional<ClientLocationDto> FindClientActiveLocation(@Param("email") String email);

}


