package com.speedKillsx.Car_Rental_API.service;

import com.speedKillsx.Car_Rental_API.dto.ClientDtoIn;
import com.speedKillsx.Car_Rental_API.dto.ClientDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Client;
import com.speedKillsx.Car_Rental_API.enums.CLIENT_STATUS;
import com.speedKillsx.Car_Rental_API.mapper.ClientMapper;
import com.speedKillsx.Car_Rental_API.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    ClientService clientService;

    @Test
    public void addClient_success() {
        ClientDtoIn clientDtoIn = ClientDtoIn.builder()
                .email("test@email.com")
                .debts(0)
                .firstName("FirstName")
                .lastName("LastName")
                .build();

        Client client = Client.builder()
                .stateClient(CLIENT_STATUS.ACTIVE)
                .debts(0)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test@email.com")
                .build();
        ClientDtoOut clientDtoOut = ClientDtoOut.builder()
                .email("test@email.com")
                .debts(0)
                .firstName("FirstName")
                .lastName("LastName")
                .build();

        when(clientMapper.toClient(clientDtoIn)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toClientDtoOut(client)).thenReturn(clientDtoOut);

        ClientDtoOut result = clientService.addClient(clientDtoIn);
        assertNotNull(result);
        assertEquals("test@email.com", result.getEmail());
        assertEquals(clientDtoOut, result);
        assertEquals(clientMapper.toClientDtoOut(client), result);
    }

    @Test
    public void getClientById_success() {
        String email = "test@email.com";
        Client client = Client.builder()
                .stateClient(CLIENT_STATUS.ACTIVE)
                .debts(0)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test@email.com")
                .build();
        ClientDtoOut clientDtoOut = ClientDtoOut.builder()
                .stateClient(CLIENT_STATUS.ACTIVE)
                .debts(0)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test@email.com")
                .build();
        when(clientRepository.getClientByEmail(email)).thenReturn(client);
        when(clientMapper.toClientDtoOut(client)).thenReturn(clientDtoOut);

        ClientDtoOut result = clientService.getClientById(email);
        assertNotNull(result);
        assertEquals(clientDtoOut, result);
        assertEquals(clientMapper.toClientDtoOut(client), result);
    }

    @Test
    public void updateClientState_success() {
        String email = "test@email.com";
        CLIENT_STATUS clientStatus = CLIENT_STATUS.PENALITY_APPLIED;

        Client client = Client.builder()
                .stateClient(CLIENT_STATUS.ACTIVE)
                .debts(0)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test@email.com")
                .build();
        ClientDtoOut clientDtoOut = ClientDtoOut.builder()
                .stateClient(clientStatus)
                .debts(0)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test@email.com")
                .build();

        when(clientRepository.getClientByEmail(email)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toClientDtoOut(client)).thenReturn(clientDtoOut);

        ClientDtoOut result = clientService.updateClientState(email, clientStatus);
        assertNotNull(result);
        assertEquals(clientDtoOut, result);
        assertEquals(clientMapper.toClientDtoOut(client), result);
        assertThat(client.getStateClient()).isEqualTo(clientStatus);
    }
}
