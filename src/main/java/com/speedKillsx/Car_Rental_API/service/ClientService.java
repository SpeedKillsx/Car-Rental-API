package com.speedKillsx.Car_Rental_API.service;

import com.speedKillsx.Car_Rental_API.dto.ClientDtoIn;
import com.speedKillsx.Car_Rental_API.dto.ClientDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Client;
import com.speedKillsx.Car_Rental_API.enums.CLIENT_STATUS;
import com.speedKillsx.Car_Rental_API.mapper.ClientMapper;
import com.speedKillsx.Car_Rental_API.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public ClientDtoOut addClient(ClientDtoIn clientDtoIn) {
        log.info("[addClient] Checking if client already exists");

        log.info("[addClient] Adding client...");

        Client client = clientMapper.toClient(clientDtoIn);
        client.setStateClient(CLIENT_STATUS.ACTIVE);
        clientRepository.save(client);
        log.info("[addClient] Client added");
        return clientMapper.toClientDtoOut(client);
    }

    public List<ClientDtoOut> getAllClients() {
        return clientMapper.toClientDtoOutList(clientRepository.findAll());
    }

    public ClientDtoOut getClientById(String email){
        Client client = clientRepository.getClientByEmail(email);
        return clientMapper.toClientDtoOut(client);
    }

    public ClientDtoOut updateClientState(String email, CLIENT_STATUS status){
        Client client = clientRepository.getClientByEmail(email);
        log.info("[updateClientState] Client found: {}", client.getEmail());
        CLIENT_STATUS precedentStatus = client.getStateClient();
        if(client == null) return null;
        client.setStateClient(status);
        log.info("[updateClientState] Client state updated from {} to {}", precedentStatus, status);
        clientRepository.save(client);
        return clientMapper.toClientDtoOut(client);
    }

}
