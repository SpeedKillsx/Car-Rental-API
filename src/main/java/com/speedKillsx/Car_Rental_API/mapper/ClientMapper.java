package com.speedKillsx.Car_Rental_API.mapper;

import com.speedKillsx.Car_Rental_API.dto.ClientDtoIn;
import com.speedKillsx.Car_Rental_API.dto.ClientDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDtoIn toClientDtoIn(Client client);

    Client toClient(ClientDtoIn clientDtoIn);

    ClientDtoOut toClientDtoOut(Client client);
    Client toClient(ClientDtoOut clientDtoOut);

    List<ClientDtoOut> toClientDtoOutList(List<Client> clients);
    List<Client> toClientList(List<ClientDtoOut> clientsDtoOut);
}
