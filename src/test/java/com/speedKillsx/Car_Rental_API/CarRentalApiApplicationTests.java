package com.speedKillsx.Car_Rental_API;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CarRentalApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
