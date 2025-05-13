package com.speedKillsx.Car_Rental_API;

import org.springframework.boot.SpringApplication;

public class TestCarRentalApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(CarRentalApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
