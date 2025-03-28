package com.esprit.microservice.Delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService implements IDriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public List<DriverDTO> getAllDrivers() {
        return driverRepository.findAll().stream()
                .map(driver -> new DriverDTO(driver.getId(), driver.getName(), driver.getPhone()))
                .collect(Collectors.toList());
    }

    @Override
    public DriverDTO createDriver(DriverDTO driverDTO) {
        Driver driver = new Driver();
        driver.setName(driverDTO.getName());
        driver.setPhone(driverDTO.getPhone());
        Driver savedDriver = driverRepository.save(driver);
        return new DriverDTO(savedDriver.getId(), savedDriver.getName(), savedDriver.getPhone());
    }
}