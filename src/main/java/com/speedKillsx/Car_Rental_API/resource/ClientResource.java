package com.speedKillsx.Car_Rental_API.resource;

import com.speedKillsx.Car_Rental_API.dto.ClientDtoIn;
import com.speedKillsx.Car_Rental_API.dto.ClientDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Client;
import com.speedKillsx.Car_Rental_API.enums.CLIENT_STATUS;
import com.speedKillsx.Car_Rental_API.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/clients")
@Tag(name = "Client", description = "Client API")
public class ClientResource {

    @Autowired
    private ClientService clientService;

    @Operation(
            summary = "Create a new Client",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Client created"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Client already exists"
                    )
            }
    )
    @PostMapping("/create")
    public ResponseEntity<ClientDtoOut> createClient(@RequestBody ClientDtoIn clientDtoIn) {
        log.info("[createClient] Creating client");
        ClientDtoOut clientDtoOut = clientService.addClient(clientDtoIn);

        if (clientDtoOut == null) {
            return new ResponseEntity<>( HttpStatus.FOUND);
        }
        return new ResponseEntity<>(clientDtoOut, HttpStatus.CREATED);
    }
    @Operation(summary = "Get all clients",
        responses = {
            @ApiResponse(responseCode = "200", description = "Clients found"),
             @ApiResponse(responseCode = "404", description = "No clients found")
        }
    )
    @GetMapping("/")
    public ResponseEntity<ClientDtoOut> findClient(String email){
        if(email == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(clientService.getClientById(email), HttpStatus.FOUND);
    }

    @Operation(summary = "Update client state",
    responses = {
            @ApiResponse(responseCode = "200", description = "Client updated"),
    })
    @GetMapping("/updateState")
    public ResponseEntity<ClientDtoOut> updateClientState(@RequestParam String email, @RequestParam  CLIENT_STATUS state){
        if(state == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(clientService.updateClientState(email, state), HttpStatus.OK);
    }
}
