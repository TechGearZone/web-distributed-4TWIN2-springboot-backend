package com.esprit.microservice.Delivery;

import java.util.List;

public interface IDriverService {
    List<DriverDTO> getAllDrivers();
    DriverDTO createDriver(DriverDTO driverDTO);
}