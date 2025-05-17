package com.speedKillsx.Car_Rental_API.service;

import com.speedKillsx.Car_Rental_API.dto.*;
import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.entity.Client;
import com.speedKillsx.Car_Rental_API.entity.Location;
import com.speedKillsx.Car_Rental_API.enums.CAR_STATUS;
import com.speedKillsx.Car_Rental_API.enums.CLIENT_STATUS;
import com.speedKillsx.Car_Rental_API.enums.LOCATION_STATE;
import com.speedKillsx.Car_Rental_API.mapper.LocationMapper;
import com.speedKillsx.Car_Rental_API.repository.CarRepository;
import com.speedKillsx.Car_Rental_API.repository.ClientRepository;
import com.speedKillsx.Car_Rental_API.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final ClientRepository clientRepository;
    private final InspectionService inspectionService;
    private final CarRepository carRepository;

    public LocationService(LocationRepository locationRepository,
                           LocationMapper locationMapper, ClientRepository clientRepository, InspectionService inspectionService, CarRepository carRepository) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.clientRepository = clientRepository;
        this.inspectionService = inspectionService;
        this.carRepository = carRepository;
    }

    public LocationDtoOut addLocation(LocationDtoIn locationDtoIn) {
        log.info("[addLocation] Adding location...");

        Location location = locationMapper.toLocation(locationDtoIn);

        // Récupération du client
        log.info("[addLocation] Searching client by id: {}", locationDtoIn.getClientId());
        Optional<Client> clientOpt = clientRepository.findById(locationDtoIn.getClientId());
        if (clientOpt.isEmpty()) {
            log.warn("[addLocation] Client not found");
            return null;
        }
        Client client = clientOpt.get();
        log.info("[addLocation] Client found: {}", client.getEmail());

        // Récupération de la voiture
        log.info("[addLocation] Searching car by matricule: {}", locationDtoIn.getCarMatricule());
        Car car = carRepository.findByMatricule(locationDtoIn.getCarMatricule());
        if (car == null) {
            log.warn("[addLocation] Car not found");
            return null;
        }
        log.info("[addLocation] Car found");

        // Calcul de la durée
        int locationDuration = location.getDateEnd().getDayOfYear() - location.getDateBegin().getDayOfYear();
        log.info("[addLocation] The location is established for {} days", locationDuration);

        Optional<ClientLocationDto> activeLocation = clientRepository.FindClientActiveLocation(client.getEmail());
        if (activeLocation.isPresent() && activeLocation.get().getNumberOfLocations() >= 2) {
            log.warn("[addLocation] Client has too many active locations: {}", activeLocation.get().getNumberOfLocations());
            return null;
        }

        if (locationDuration > 30) {
            log.warn("[addLocation] Location duration exceeds 30 days");
            return null;
        }

        if (locationDuration < 1) {
            log.warn("[addLocation] Location duration is less than 1 day");
            return null;
        }

        if (car.getState() == CAR_STATUS.ON_LOCATION) {
            log.warn("[addLocation] Car is already on location");
            return null;
        }

        if (client.getStateClient() == CLIENT_STATUS.DEBT || client.getStateClient() == CLIENT_STATUS.CAUTION_BLOCKED) {
            log.warn("[addLocation] Client is not eligible (debt or caution blocked)");
            return null;
        }

        if (!inspectionService.isCarWithinAllowedRentalPeriod(car.getMatricule())) {
            log.warn("[addLocation] Car has not driven enough km for a new rental");
            return null;
        }

        log.info("[addLocation] All business rules passed");

        location.setCar(car);
        location.setClient(client);
        location.setLocationState(LOCATION_STATE.ACTIVE);
        log.info("[addLocation] Location ID {} ", location.getId());
        locationRepository.save(location);
        log.info("[addLocation] Location saved");

        car.setState(CAR_STATUS.ON_LOCATION);
        car.getLocation().add(location);
        carRepository.save(car);
        log.info("[addLocation] Car state updated and saved");

        log.info("[addLocation] Location creation process finished");
        return locationMapper.toLocationDtoOut(location);
    }


    public List<LocationDtoOut> getAllLocations(String email) {
        log.info("[getAllLocations] Getting all locations for client {}", email);
        Client client = clientRepository.getClientByEmail(email);
        if (client == null) return null;


        if (client.getLocation().isEmpty()) {
            return List.of();
        }

        log.info("[getAllLocations] Found {} locations", client.getLocation().size());
        List<Location> locations = new java.util.ArrayList<>(List.of());
        for (Location location : locationRepository.findLocationsByClient_Email(email)) {
            if (location.getLocationState().equals(LOCATION_STATE.ACTIVE)) {
                locations.add(location);
            }
        }
        log.info("[getAllLocations] Found {} locations", locations.size());
        return locationMapper.toLocationDtoOutList(locations);
    }

    public RestitutionDtoOut carRestitution(RestitutionDTOIn restitutionDTOIn) {
        log.info("[carRestitution] Searching for car");
        RestitutionDtoOut restitutionDtoOut = locationRepository.findRentedCar(restitutionDTOIn.getDateBegin(),
                restitutionDTOIn.getDateEnd(),
                restitutionDTOIn.getCarMatricule());
        restitutionDtoOut.setDateRestitution(restitutionDTOIn.getDateRestitution());

        Optional<Location> location = locationRepository.findById(restitutionDtoOut.getId());
        Optional<Client> client = clientRepository.findById(restitutionDtoOut.getClientId());

        if (location.isEmpty() || client.isEmpty()) {
            if (location.isEmpty()) {
                log.warn("[carRestitution] Location not found");
                return null;
            }
            log.warn("[carRestitution] Client not found");
            return null;
        }

        log.info("[carRestitution] Location found with id {}", location.get().getId());
        boolean applyPenality = isPenalityNeeded(restitutionDtoOut.getDateEnd(), restitutionDtoOut.getDateRestitution());

        manageRestituiton(location.get(), client.get(), applyPenality);
        restitutionDtoOut.setClientStatus(client.get().getStateClient());
        restitutionDtoOut.setLocationState(location.get().getLocationState());
        log.info("[carRestitution] Restitution process finished");
        return restitutionDtoOut;


    }

    public boolean isPenalityNeeded(LocalDate endRentDate, LocalDate restitutionDate) {
        log.info("[isPenalityNeeded] Checking if restitution date exceeds 3 days...");
        if (restitutionDate == null) return false;
        return restitutionDate.getDayOfYear() - endRentDate.getDayOfYear() > 3;
    }

    public void manageRestituiton(Location location, Client client, boolean isPenalityApplied) {
        if (isPenalityApplied) {
            log.info("[manageRestituiton] Restitution date exceeds 3 days, penalities will be applied");
            location.setLocationState(LOCATION_STATE.FINISHED);
            log.info("[manageRestituiton] Penality location state updated to FINISHED");
            locationRepository.save(location);
            log.info("[manageRestituiton] Location is finished");
            client.setStateClient(CLIENT_STATUS.PENALITY_APPLIED);
            float clientDebts = location.getAmount().add(
                    location.getAmount().multiply(BigDecimal.valueOf(0.25))
            ).floatValue();
            client.setDebts(clientDebts);
            log.info("[manageRestituiton] Client {} :  state updated to PENALITY_APPLIED", client.getEmail());
            clientRepository.save(client);
            log.info("[manageRestituiton] Client {} : debt applied", client.getEmail());
        } else {
            log.info("[manageRestituiton] Restitution date is within 3 days, penalities will not be applied");
            location.setLocationState(LOCATION_STATE.FINISHED);
            locationRepository.save(location);
            log.info("[manageRestituiton] Location state updated to FINISHED");
            client.setStateClient(CLIENT_STATUS.ACTIVE);
            client.setDebts(0);
            log.info("[manageRestituiton] Client {} :  state updated to ACTIVE", client.getEmail());
            log.info("[manageRestituiton] Client {} : debt set to 0", client.getEmail());
            clientRepository.save(client);

        }
    }
}
