package com.speedKillsx.Car_Rental_API.integration.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.speedKillsx.Car_Rental_API.dto.CarDtoIn;
import com.speedKillsx.Car_Rental_API.dto.CarDtoOut;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Slf4j
public class CarResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    @Rollback(true)
    @Order(2)
    public void createCar_shouldReturnAccepted() throws Exception {
        CarDtoIn carDtoIn = CarDtoIn.builder()
                .matricule("87887")
                .model("BMW")
                .distanceTraveled(0L)
                .build();

        CarDtoOut carDtoOut = CarDtoOut.builder()
                .matricule("87887")
                .model("BMW")
                .distanceTraveled(0L)
                .build();
        String result = mockMvc.perform(post("/api/v1/cars/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carDtoIn)))
                .andExpect(status().isAccepted())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CarDtoOut resultCar = objectMapper.readValue(result, CarDtoOut.class);
        log.info("[createCar] Result: {}", resultCar.getMatricule());
        assertNotNull(resultCar);
        assertNotNull(resultCar.getMatricule());
        assertNotNull(resultCar.getModel());
        assertNotNull(resultCar.getDistanceTraveled());
        assertEquals(resultCar.getMatricule(), carDtoOut.getMatricule());
        assertEquals(resultCar.getModel(), carDtoOut.getModel());
        assertEquals(resultCar.getDistanceTraveled(), carDtoOut.getDistanceTraveled());
    }

    @Test
    @Transactional
    @Rollback(true)
    @Order(1)
    public void getAllCars_shouldReturnOk() throws Exception {


        String result = mockMvc.perform(get("/api/v1/cars/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<CarDtoOut> resultList = objectMapper.readValue(result, List.class);
        log.info("[getAllCars] Result: {}", result);
        assertNotNull(result);
        assertEquals(resultList.size(), 6);


    }
}
