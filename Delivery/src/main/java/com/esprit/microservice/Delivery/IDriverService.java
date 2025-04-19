package com.esprit.microservice.Delivery;

import java.util.List;

public interface IDriverService {
    List<DriverDTO> getAllDrivers();
    DriverDTO createDriver(DriverDTO driverDTO);
    DriverDTO updateDriver(Long id, DriverDTO driverDTO);
    void deleteDriver(Long id);
    DriverDTO getDriverById(Long id); // Add this new method
}