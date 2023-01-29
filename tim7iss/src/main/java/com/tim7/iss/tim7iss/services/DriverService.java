package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.Driver;
import com.tim7.iss.tim7iss.models.Enums;
import com.tim7.iss.tim7iss.models.Ride;
import com.tim7.iss.tim7iss.models.Role;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.repositories.RoleRepository;
import com.tim7.iss.tim7iss.repositories.WorkHourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService {
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private WorkHourRepository workHourRepository;

    public void save(Driver driver){
        driverRepository.save(driver);
    }

    public Driver findById(Long id) throws DriverNotFoundException {
        Driver driver = driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);
        return  driver;
    }

    public Collection<Driver> getAll() {
        return driverRepository.findAll();
    }

    public Page<Driver> getAll(Pageable pageable) {
        return driverRepository.findAll(pageable);
    }

    public Optional<Driver> getById(Long id) {
        return driverRepository.findById(id);
    }

    public Optional<Driver> getByEmailAddress(String emailAddress) {
        return driverRepository.findByEmailAddress(emailAddress);
    }

    // ------------------- Refactored methods --------------------------------

    public Driver createAccount(UserDto driverDto)
            throws EmailAlreadyExistsException, NotAnImageException, LargeImageException {
        Optional<Driver> driverByEmail = driverRepository.findByEmailAddress(driverDto.getEmail());
        if (driverByEmail.isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        // checking the validity of the passed image
        if (!driverDto.getProfilePicture().isBlank()) {
            if (!imageService.isImageValid(driverDto.getProfilePicture())) {
                throw new NotAnImageException();
            }
            if (driverDto.getProfilePicture().getBytes().length > Constants.imageFieldSize) {
                throw new LargeImageException();
            }
        }

        Driver newDriver = new Driver(driverDto);
        newDriver.setPassword(BCrypt.hashpw(driverDto.getPassword(), BCrypt.gensalt()));

        Role driverRole = roleRepository.findByName("ROLE_DRIVER").orElse(null);
        assert driverRole != null;

        newDriver.setRoles(List.of(driverRole));
        newDriver.setEnabled(true);
        return driverRepository.save(newDriver);
    }

    public Driver makeChangesToAccount(Long id, UserDto driverChanges)
            throws NotAnImageException, LargeImageException, DriverNotFoundException {

        // checking the validity of the passed image
        if (!driverChanges.getProfilePicture().isBlank()) {
            if (!imageService.isImageValid(driverChanges.getProfilePicture())) {
                throw new NotAnImageException();
            }
            if (driverChanges.getProfilePicture().getBytes().length > Constants.imageFieldSize) {
                throw new LargeImageException();
            }
        }

        Driver driver = driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);
        driver.setFirstName(driverChanges.getName());
        driver.setLastName(driverChanges.getSurname());
        driver.setProfilePicture(driverChanges.getProfilePicture());
        driver.setPhoneNumber(driverChanges.getTelephoneNumber());
        driver.setEmailAddress(driverChanges.getEmail());
        driver.setAddress(driverChanges.getAddress());
        return driverRepository.save(driver);
    }

    public PaginatedResponseDto<UserDto> getPaginatedDriversAsDto(Pageable page) {
        Collection<UserDto> drivers = driverRepository.findAll(page)
                .stream()
                .map(UserDto::new)
                .toList();
        return new PaginatedResponseDto<>(drivers.size(), drivers);
    }

    public UserDto getDriverDtoById(Long id) throws DriverNotFoundException {
         Driver driver = driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);
         return new UserDto(driver);
    }

    public Collection<DriverDocumentDto> getDriverDocumentsAsDto(Long id) throws DriverNotFoundException {
        Driver driver = driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);
        return driver.getDocuments()
                .stream()
                .map(DriverDocumentDto::new)
                .toList();
    }

    public VehicleDto getAssignedVehicle(Long id) throws DriverNotFoundException, VehicleNotAssignedException {
        Driver driver = driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);
        if (driver.getVehicle() == null) {
            throw new VehicleNotAssignedException();
        }
        return new VehicleDto(driver.getVehicle());
    }

    // returns work hours for the driver with the passed id
    public PaginatedResponseDto<WorkingHourDto> getPaginatedWorkingHoursAsDto(Long id, Pageable page) throws DriverNotFoundException {
        driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);
        List<WorkingHourDto> workHours = workHourRepository.findByDriverId(id, page)
                .stream()
                .map(WorkingHourDto::new)
                .toList();
        return new PaginatedResponseDto<WorkingHourDto>(workHours.size(), workHours);
    }

    public ActivityDto getIsActiveAsDto(Long id) throws DriverNotFoundException {
        Driver driver = driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);
        return new ActivityDto(driver.isActive());
    }

    public Driver setActivityState(ActivityDto activityDto, Long id) throws DriverNotFoundException {
        Driver driver = driverRepository.findById(id).orElseThrow(DriverNotFoundException::new);
        driver.setActive(activityDto.getIsActive());
        return driverRepository.save(driver);
    }

    // return the current locations of drivers
    public PaginatedResponseDto<DriverLocationDto> getPaginatedLocationsAsDto(Pageable page) {
        Collection<DriverLocationDto> driverLocations = driverRepository.findAll(page)
                .stream()
                .map(DriverLocationDto::new)
                .toList();
        return new PaginatedResponseDto<>(driverLocations.size(), driverLocations);
    }

    // ----------------------------------------------------------------------

}
