package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.VehicleDto;
import com.tim7.iss.tim7iss.exceptions.DriverNotFoundException;
import com.tim7.iss.tim7iss.exceptions.VehicleNotAssignedException;
import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.Location;
import com.tim7.iss.tim7iss.models.Vehicle;
import com.tim7.iss.tim7iss.models.VehicleType;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.VehicleRepository;
import com.tim7.iss.tim7iss.repositories.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;

    public Vehicle createAndAssignToDriver(VehicleDto vehicleDto, Long driverId) throws DriverNotFoundException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);

        if (driver.getVehicle() != null) {
            Vehicle driverOldVehicle = driver.getVehicle();
            driverOldVehicle.setDriver(null);
            vehicleRepository.save(driverOldVehicle);
        }

        VehicleType vehicleType = vehicleTypeRepository.findByName(vehicleDto.getVehicleType()).orElse(null);
        assert vehicleType != null;

        Location vehicleCurrentLocation = new Location(vehicleDto.getCurrentLocation());

        Vehicle newVehicle = new Vehicle(vehicleDto, vehicleType, driver, vehicleCurrentLocation);
        driver.setVehicle(newVehicle);
        return vehicleRepository.save(newVehicle);
    }

    public Vehicle makeChanges(VehicleDto vehicleDto, Long driverId)
            throws DriverNotFoundException, VehicleNotAssignedException {
        Driver driver = driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);
        if (driver.getVehicle() == null) {
            throw new VehicleNotAssignedException();
        }

        VehicleType vehicleType = vehicleTypeRepository.findByName(vehicleDto.getVehicleType()).orElse(null);
        assert vehicleType != null;

        Location newCurrentLocation = new Location(vehicleDto.getCurrentLocation());

        Vehicle updatedVehicle = new Vehicle(vehicleDto, vehicleType, driver, newCurrentLocation);
        updatedVehicle.setId(driver.getVehicle().getId());
        return vehicleRepository.save(updatedVehicle);
    }
}
