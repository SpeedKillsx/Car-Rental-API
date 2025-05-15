package com.speedKillsx.Car_Rental_API.service;

import com.speedKillsx.Car_Rental_API.dto.ClientLocationDto;
import com.speedKillsx.Car_Rental_API.dto.LocationDtoIn;
import com.speedKillsx.Car_Rental_API.dto.LocationDtoOut;
import com.speedKillsx.Car_Rental_API.entity.Car;
import com.speedKillsx.Car_Rental_API.entity.Client;
import com.speedKillsx.Car_Rental_API.entity.Location;
import com.speedKillsx.Car_Rental_API.enums.CAR_STATUS;
import com.speedKillsx.Car_Rental_API.enums.CLIENT_STATUS;
import com.speedKillsx.Car_Rental_API.enums.LOCATION_STATE;
import com.speedKillsx.Car_Rental_API.mapper.LocationMapper;
import com.speedKillsx.Car_Rental_API.repository.ClientRepository;
import com.speedKillsx.Car_Rental_API.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final ClientRepository clientRepository;
    private final InspectionService inspectionService;

    public LocationService(LocationRepository locationRepository,
                           LocationMapper locationMapper, ClientRepository clientRepository, InspectionService inspectionService) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.clientRepository = clientRepository;
        this.inspectionService = inspectionService;
    }

    public LocationDtoOut addLocation(LocationDtoIn locationDtoIn) {
        log.info("[addLocation] Adding location...");
        Location location = locationMapper.toLocation(locationDtoIn);
        log.info("[addLocation] Searching client by id: {}", locationDtoIn.getClientId());

        Optional<Client> client = clientRepository.findById(locationDtoIn.getClientId());
        int locationDuration = location.getDateBegin().getDayOfYear() - location.getDateEnd().getDayOfYear();
        log.info("[addLocation] The location is etablish for {} days", locationDuration);
        if (client.isEmpty()) return null;

        Optional<ClientLocationDto> activeLocation = clientRepository.FindClientActiveLocation(client.get().getEmail());

        if (activeLocation.isPresent() && activeLocation.get().getNumberOfLocations() > 0) {
            log.warn("[addLocation] Client already has an active location {} , He cannot have more than {} active locations"
                    , activeLocation.get().getNumberOfLocations(), activeLocation.get().getNumberOfLocations());
            return null;
        }

        if (locationDuration > 30) {
            log.warn("[addLocation] The location is etablish for more than 30 days");
            return null;
        } else if (location.getCar().getState().equals(CAR_STATUS.ON_LOCATION.toString())) {
            log.warn("[addLocation] The car is already on location");
            return null;
        } else if (locationDuration < 1) {
            log.warn("[addLocation] The location is etablish for less than 1 day");
        } else if (client.get().getStateClient().equals(CLIENT_STATUS.DEBT.toString())
                || client.get().getStateClient().equals(CLIENT_STATUS.CAUTION_BLOCKED.toString())) {
            log.warn("[addLocation] Client is in debt or caution blocked state");
            return null;
        }
        if(!inspectionService.isCarWithinAllowedRentalPeriod(location.getCar().getMatricule())){
            return null;
        }
        locationRepository.save(location);
        Car carLocation = location.getCar();
        carLocation.setState(CAR_STATUS.ON_LOCATION);
        carLocation.getLocation().add(location);
        log.info("[addLocation] Location added");
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
            if (location.getLocationState().equals(LOCATION_STATE.ACTIVE.toString())) {
                locations.add(location);
            }
        }
        log.info("[getAllLocations] Found {} locations", locations.size());
        return locationMapper.toLocationDtoOutList(locations);
    }
}
