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

import java.util.List;

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
                .email("test2@email.com")
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

    @Test
    public  void getAllClients_success(){
        Client client1 = Client.builder()
                .stateClient(CLIENT_STATUS.ACTIVE)
                .debts(0)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test@email.com")
                .build();

        Client client2 = Client.builder()
                .stateClient(CLIENT_STATUS.PENALITY_APPLIED)
                .debts(0)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test2@email.com")
                .build();

        List<Client> clients = List.of(client1, client2);

        ClientDtoOut clientDtoOut1 = ClientDtoOut.builder()
                .stateClient(CLIENT_STATUS.ACTIVE)
                .debts(0)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test@email.com")
                .build();

        ClientDtoOut clientDtoOut2 = ClientDtoOut.builder()
                .stateClient(CLIENT_STATUS.PENALITY_APPLIED)
                .firstName("FirstName")
                .lastName("LastName")
                .email("test2@email.com")
                .build();
        List<ClientDtoOut> clientDtoOutList = List.of(clientDtoOut1, clientDtoOut2);

        when(clientRepository.findAll()).thenReturn(clients);
        when(clientMapper.toClientDtoOutList(clients)).thenReturn(clientDtoOutList);

        List<ClientDtoOut> result = clientService.getAllClients();
        assertNotNull(result);
        assertEquals(clientDtoOutList, result);
        assertEquals(clientMapper.toClientDtoOutList(clients), result);
        assertEquals(client1.getStateClient(), CLIENT_STATUS.ACTIVE);
        assertEquals(client2.getStateClient(), CLIENT_STATUS.PENALITY_APPLIED);
        assertEquals(client1.getEmail(), clientDtoOut1.getEmail());
        assertEquals(client2.getEmail(), clientDtoOut2.getEmail());
    }
}
