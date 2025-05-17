package com.speedKillsx.Car_Rental_API.integration.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speedKillsx.Car_Rental_API.dto.LocationDtoIn;
import com.speedKillsx.Car_Rental_API.dto.LocationDtoOut;
import com.speedKillsx.Car_Rental_API.dto.RestitutionDTOIn;
import com.speedKillsx.Car_Rental_API.dto.RestitutionDtoOut;
import com.speedKillsx.Car_Rental_API.enums.CLIENT_STATUS;
import com.speedKillsx.Car_Rental_API.enums.LOCATION_STATE;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class LocationIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Transactional
    @Rollback(true)
    @Order(1)
    public void createLocation_shouldReturnCreated() throws Exception {
        LocationDtoIn locationDtoIn = LocationDtoIn.builder()
                .dateBegin(LocalDate.of(2025, 5, 15))
                .dateEnd(LocalDate.of(2025, 5, 20))
                .amount(BigDecimal.valueOf(10000.00))
                .clientId(1)
                .carMatricule("XYZ-789")
                .build();

        LocationDtoOut locationDtoOut = LocationDtoOut.builder()
                .locationState(LOCATION_STATE.ACTIVE)
                .id(6)
                .dateBegin(LocalDate.of(2025, 5, 15))
                .dateEnd(LocalDate.of(2025, 5, 20))
                .amount(BigDecimal.valueOf(10000.00))
                .clientId(1)
                .carMatricule("XYZ-789")
                .build();

        String result = mockMvc.perform(
                        post("/api/v1/location/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(locationDtoIn)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();


        LocationDtoOut resultDtoOut = objectMapper.readValue(result, LocationDtoOut.class);

        log.info("[createLocation] Result: {}", resultDtoOut);
        assertNotNull(resultDtoOut);
        assertEquals(locationDtoOut.getLocationState(), resultDtoOut.getLocationState());

    }

    @Test
    @Transactional
    @Rollback(true)
    @Order(2)
    public void restituateCar_NoPenality_shouldReturnOk() throws Exception {
        RestitutionDTOIn restitutionDTOIn = RestitutionDTOIn.builder()
                .clientId(2)
                .dateBegin(LocalDate.of(2025, 6, 20))
                .dateEnd(LocalDate.of(2025, 7, 18))
                .carMatricule("ABC-123")
                .dateRestitution(LocalDate.now())
                .build();

        RestitutionDtoOut restitutionDtoOut = RestitutionDtoOut.builder()
                .clientId(2)
                .dateBegin(LocalDate.of(2025, 6, 20))
                .dateEnd(LocalDate.of(2025, 7, 18))
                .dateRestitution(LocalDate.now())
                .carMatricule("ABC-123")
                .build();

        String result = mockMvc.perform(
                        post("/api/v1/location/restituate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(restitutionDTOIn))
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RestitutionDtoOut resultDtoOut = objectMapper.readValue(result, RestitutionDtoOut.class);
        log.info("[restituateCar] Result: {}", resultDtoOut);
        assertNotNull(resultDtoOut);
        assertEquals(restitutionDtoOut.getDateRestitution(), resultDtoOut.getDateRestitution());

    }

    @Test
    @Transactional
    @Rollback(true)
    @Order(3)
    public void restituateCar_Penality_shouldReturnOk() throws Exception {
        RestitutionDTOIn restitutionDTOIn = RestitutionDTOIn.builder()
                .clientId(2)
                .dateBegin(LocalDate.of(2025, 7, 20))
                .dateEnd(LocalDate.of(2025, 8, 19))
                .carMatricule("POP-852")
                .dateRestitution(LocalDate.of(2025, 9, 20))
                .build();

        RestitutionDtoOut restitutionDtoOut = RestitutionDtoOut.builder()
                .clientId(2)
                .dateBegin(LocalDate.of(2025, 7, 20))
                .dateEnd(LocalDate.of(2025, 8, 19))
                .dateRestitution(LocalDate.of(2025, 9, 20))
                .clientStatus(CLIENT_STATUS.PENALITY_APPLIED)
                .carMatricule("POP-852")
                .build();

        String result = mockMvc.perform(
                        post("/api/v1/location/restituate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(restitutionDTOIn))
                ).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        RestitutionDtoOut resultDtoOut = objectMapper.readValue(result, RestitutionDtoOut.class);
        log.info("[restituateCar] Result: {}", resultDtoOut);
        assertNotNull(resultDtoOut);
        assertEquals(restitutionDtoOut.getDateRestitution(), resultDtoOut.getDateRestitution());
        assertEquals(restitutionDtoOut.getClientStatus(), resultDtoOut.getClientStatus());
    }

}
