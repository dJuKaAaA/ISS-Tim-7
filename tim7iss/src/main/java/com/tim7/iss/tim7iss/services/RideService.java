package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.DriverRepository;
import com.tim7.iss.tim7iss.repositories.RideRepository;
import com.tim7.iss.tim7iss.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class RideService {


    @Autowired
    DriverService driverService;
    @Autowired
    PassengerService passengerService;
    @Autowired
    FavoriteLocationService favoriteLocationService;
    @Autowired
    VehicleTypeService vehicleTypeService;
    @Autowired
    MapService mapService;
    @Autowired
    WorkHourService workHourService;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private UserRepository userRepository;

    public void save(Ride ride) {
        rideRepository.save(ride);
    }


    public List<Ride> findRideByPassengerId(Long id) {
        return rideRepository.findAll(id);
    }


    public List<Ride> findByPassengerIdAndStatus(Long id, Integer status) throws RideNotFoundException {
        return rideRepository.findByPassengersIdAndStatus(id, status).orElseThrow(RideNotFoundException::new);
    }

    public Ride findById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }


    public Ride driverRideAtMoment(Long driverId, LocalDateTime moment) {
        List<Ride> rides = rideRepository.findRidesByDriverId(driverId);
        rides = rides.stream().filter((Ride ride) -> {
            if (ride.getStatus() == Enums.RideStatus.ACCEPTED || ride.getStatus() == Enums.RideStatus.PENDING || ride.getStatus() == Enums.RideStatus.ACTIVE) {
                LocalDateTime rideStartTime = ride.getStartTime();
                LocalDateTime estimatedRideEndTime = ride.getStartTime().plusMinutes(ride.getEstimatedTimeInMinutes());

                // the time window for in which a ride is considered to be taken is between 5 minutes before start date and 5 minutes after end date
                return !moment.isBefore(rideStartTime.minusMinutes(Constants.vehicleWaitTimeInMinutes)) && !moment.isAfter(estimatedRideEndTime.plusMinutes(Constants.vehicleWaitTimeInMinutes));
            }
            return false;
        }).toList();
        return rides.size() == 0 ? null : rides.get(0);
    }

    public boolean AnyRidesArePending(List<UserRefDto> passengers) throws RideNotFoundException {
        for (UserRefDto passenger : passengers) {
            List<Ride> rides = findByPassengerIdAndStatus(passenger.getId(), Enums.RideStatus.PENDING.ordinal());
            if (rides.size() != 0) return true;
        }
        return false;
    }


    //Milos
    public boolean checkIfMoreThan9FavoriteLocations(Long id, FavoriteLocationService favoriteLocationService) throws UserNotFoundException {
        List<FavoriteLocation> locations = favoriteLocationService.findByPassengerId(id);
        return locations.size() > 9;
    }

    public FavoriteLocationDto createFavoriteLocation(String passengerEmail, FavoriteLocationDto favoriteLocationDto) throws PassengerNotFoundException, TooManyFavoriteRidesException, UserNotFoundException {
        if(passengerEmail == null || favoriteLocationDto == null)
            throw new NullPointerException("Passed values are null");
        Passenger passengerThatSubmitted = passengerService.findByEmailAddress(passengerEmail);
        if (checkIfMoreThan9FavoriteLocations(passengerThatSubmitted.getId(), favoriteLocationService)) {
            throw new TooManyFavoriteRidesException();
        }
        Set<Passenger> passengers = addPassengerFromFavoriteLocation(favoriteLocationDto);
        VehicleType vehicleType = vehicleTypeService.getByName(favoriteLocationDto.getVehicleType());
        FavoriteLocation favoriteLocation = new FavoriteLocation(favoriteLocationDto, passengers, vehicleType, passengerThatSubmitted);
        favoriteLocation = favoriteLocationService.save(favoriteLocation);
        return new FavoriteLocationDto(favoriteLocation);
    }

    public List<FavoriteLocationDto> getFavoriteLocations() {
        List<FavoriteLocation> favoriteLocations = favoriteLocationService.getAll();
        List<FavoriteLocationDto> favoriteLocationsDto = new ArrayList<>();
        for (FavoriteLocation favoriteLocation : favoriteLocations) {
            favoriteLocationsDto.add(new FavoriteLocationDto(favoriteLocation));
        }
        return favoriteLocationsDto;
    }

    //Milos
    public FavoriteLocationDto getFavoriteLocationById(Long id) throws FavoriteLocationNotFoundException {
        FavoriteLocation favoriteLocation = favoriteLocationService.findById(id);
        if (favoriteLocation == null) throw new FavoriteLocationNotFoundException();
        return new FavoriteLocationDto(favoriteLocation);
    }

    //Milos
    public List<FavoriteLocationDto> getFavoriteLocationsByPassengerId(Long id) throws UserNotFoundException {
        List<FavoriteLocation> favoriteLocations = favoriteLocationService.findByPassengerId(id);
        List<FavoriteLocationDto> favoriteLocationsDto = new ArrayList<>();
        for (FavoriteLocation favoriteLocation : favoriteLocations) {
            favoriteLocationsDto.add(new FavoriteLocationDto(favoriteLocation));
        }
        return favoriteLocationsDto;
    }

    //Milos
    public void deleteFavoriteLocation(Long id) throws FavoriteLocationNotFoundException {
        if (favoriteLocationService.findById(id) == null) throw new FavoriteLocationNotFoundException();
        favoriteLocationService.delete(id);
    }

    //Milos
    public List<RideDto> getDriversActiveRide(Long driverId) throws UserNotFoundException, RideNotFoundException {
        driverService.getById(driverId).orElseThrow(() -> new UserNotFoundException("Driver not found"));
        List<Ride> rides = rideRepository.findByDriverIdAndStatus(driverId, Enums.RideStatus.ACTIVE.ordinal()).orElseThrow(RideNotFoundException::new);
        List<RideDto> rideDtos = rides.stream().map(RideDto::new).toList();
        if (rideDtos.size() == 0) {
            throw new RideNotFoundException("Active ride does not exist!");
        }
        return rideDtos;
    }

    //Milos
    public List<RideDto> getPassengersActiveRide(Long passengerId) throws RideNotFoundException {
        List<Ride> rides = rideRepository.findByPassengersIdAndStatus(passengerId, Enums.RideStatus.ACTIVE.ordinal()).orElseThrow(RideNotFoundException::new);
        List<RideDto>rideDtos = rides.stream().map(RideDto::new).toList();
        if (rideDtos.size() == 0) {
            throw new RideNotFoundException("Active ride does not exist!");
        }
        return rideDtos;
    }

    public RideDto getRideById(Long id) throws RideNotFoundException {
        Ride ride = rideRepository.findById(id).orElseThrow(RideNotFoundException::new);
        return new RideDto(ride);
    }

    //Milos
    public RideDto cancelRideById(Long id) throws RideNotFoundException, RideCancelationException {
        Ride ride = rideRepository.findById(id).orElseThrow(RideNotFoundException::new);
        if (ride.getStatus() != Enums.RideStatus.PENDING && ride.getStatus() != Enums.RideStatus.ACTIVE)
            throw new RideCancelationException();
        ride.setStatus(Enums.RideStatus.REJECTED);
        save(ride);
        return new RideDto(ride);
    }

    //Milos
    public PanicDetailsDto creatingPanicProcedure(UserService userService, PanicService panicService, String userEmail, Long rideId, PanicCreateDto reason) throws UserNotFoundException, RideNotFoundException {
        User user = userService.findByEmailAddress(userEmail).orElseThrow(UserNotFoundException::new);
        Ride ride = rideRepository.findById(rideId).orElseThrow(RideNotFoundException::new);
        Panic panic = new Panic(reason, ride, user);
        panicService.save(panic);
        return new PanicDetailsDto(panic);
    }

    //Milos
    public RideDto acceptRide(Long id, String userEmail) throws RideNotFoundException, RideCancelationException, DriverNotFoundException {
        Ride ride = rideRepository.findById(id).orElseThrow(RideNotFoundException::new);
        User driver = driverService.getByEmailAddress(userEmail).orElseThrow(DriverNotFoundException::new);

        if (ride.getDriver().getId() != driver.getId()) throw new RideNotFoundException();

        if (ride.getStatus() != Enums.RideStatus.PENDING)
            throw new RideCancelationException("Cannot accept a ride that is not in status PENDING!");

        ride.setStatus(Enums.RideStatus.ACCEPTED);
        rideRepository.save(ride);
        return new RideDto(ride);
    }


    // Testirano
    public RideDto endRide(Long id, String userEmail) throws RideNotFoundException, RideCancelationException, DriverNotFoundException {
        Ride ride = rideRepository.findById(id).orElseThrow(RideNotFoundException::new);
        User driver = driverService.getByEmailAddress(userEmail).orElseThrow(DriverNotFoundException::new);

        if (ride.getDriver().getId() != driver.getId()) throw new RideNotFoundException();

        if (ride.getStatus() != Enums.RideStatus.ACTIVE)
            throw new RideCancelationException("Cannot end a ride that is not in status STARTED!");
        ride.setStatus(Enums.RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());  // ride finishes as soon as its status is set to finished
        save(ride);
        return new RideDto(ride);
    }

    // Testirano
    public RideDto rejectRide(Long id, String userEmail, PanicCreateDto rideReject) throws RideNotFoundException, RideCancelationException, DriverNotFoundException {
        Ride ride = rideRepository.findById(id).orElseThrow(RideNotFoundException::new);
        User driver = driverService.getByEmailAddress(userEmail).orElseThrow(DriverNotFoundException::new);

        if (ride.getDriver().getId() != driver.getId()) throw new RideNotFoundException();

        if (ride.getStatus() != Enums.RideStatus.PENDING && ride.getStatus() != Enums.RideStatus.ACCEPTED)
            throw new RideCancelationException("Cannot cancel a ride that is not in status PENDING or ACCEPTED!");

        Refusal refusal = new Refusal(rideReject);
        refusal.setRide(ride);
        ride.setRefusal(refusal);
        ride.setStatus(Enums.RideStatus.CANCELED);  // Ovo je nekad bilo REJECTED ali sam stavio na CANCELED jer ima vise smisla
        save(ride);
        return new RideDto(ride);
    }

    // Testirano
    public RideDto startRide(Long id, String userEmail) throws RideNotFoundException, RideCancelationException, DriverNotFoundException {
        Ride ride = rideRepository.findById(id).orElseThrow(RideNotFoundException::new);
        User driver = driverService.getByEmailAddress(userEmail).orElseThrow(DriverNotFoundException::new);

        if (ride.getDriver().getId() != driver.getId()) throw new RideNotFoundException();

        if (ride.getStatus() != Enums.RideStatus.ACCEPTED)
            throw new RideCancelationException("Cannot start a ride that is not in status ACCEPTED!");
        ride.setStatus(Enums.RideStatus.ACTIVE);
        save(ride);
        return new RideDto(ride);
    }

    // Testirano
    public RideDto setDriver(RideAddDriverDto rideAddDriverDto) throws DriverNotFoundException, RideNotFoundException {
        Ride ride = rideRepository.findById(rideAddDriverDto.getDriverId()).orElseThrow(RideNotFoundException::new);
        Driver driver = driverService.findById(rideAddDriverDto.getDriverId());
        ride.setDriver(driver);
        save(ride);
        return new RideDto(ride);
    }


    public RideDto scheduleRide(RideCreationDto rideCreationDto) throws RideAlreadyPendingException, SchedulingRideAtInvalidDateException, DriverNotFoundException, RideNotFoundException {
        if (AnyRidesArePending(rideCreationDto.getPassengers()));
        // driver that doesn't have an active ride at the moment
        Driver availablePotentialDriver = null;
        Integer distanceFromStartLocationAvailableDriver = null;

        // driver that has an active ride at the moment but is 5 minutes away from finishing
        Driver currentlyUnavailablePotentialDriver = null;
        List<Driver> currentlyUnavailablePotentialDrivers = new ArrayList<>();
        Integer distanceFromStartLocationUnavailableDriver = null;

        // initializing the ride according to the ride creation dto
        Ride rideToSchedule = new Ride(rideCreationDto);

        // throwing error if the schedule date is invalid
        if (rideToSchedule.getStartTime().isBefore(LocalDateTime.now().plusMinutes(Constants.vehicleWaitTimeInMinutes))) {
            throw new SchedulingRideAtInvalidDateException("Ride can only be scheduled " + Constants.vehicleWaitTimeInMinutes + " minutes from now or later");
        }

        // additional necessary information
        Location rideStartLocation = new Location(rideCreationDto.getLocations().get(0).getDeparture());
        LocalDateTime estimatedRequestedEndTime = rideToSchedule.getStartTime().plusMinutes(rideToSchedule.getEstimatedTimeInMinutes());

        for (Driver driver : driverService.getAll()) {

            // if driver is inactive: check next driver
            if (!driver.isActive()) {
                continue;
            }

            // if the has an ongoing shift but has worked more than 8 hours: check next driver
            if (workHourService.hoursWorked(driver.getId(), LocalDate.now()) >= 8) {
                continue;
            }

            // checking to see if the ride that is about to get scheduled is going to overlap with an already existing ride
            Ride scheduledRideAtEstimatedEndTime = driverRideAtMoment(driver.getId(), estimatedRequestedEndTime);

            // if estimated end time is overlapping with an existing ride: check next driver
            if (scheduledRideAtEstimatedEndTime != null) {
                continue;
            }

            // getting the ride scheduled at the time this passenger is requesting
            Ride alreadyScheduledRide = driverRideAtMoment(driver.getId(), rideToSchedule.getStartTime());

            // if there is a ride already scheduled at that date: check next driver
            if (alreadyScheduledRide != null) {
                /* driver is put into consideration to be given the new scheduled ride
                 * only if he is Constants.vehicleWaitTimeInMinutes minutes away from finishing his currently active ride */
                if (alreadyScheduledRide.getStatus() == Enums.RideStatus.ACTIVE) {
                    LocalDateTime estimatedRideEndTime = alreadyScheduledRide.getStartTime().plusMinutes(alreadyScheduledRide.getEstimatedTimeInMinutes());
                    long minutesUntil = LocalDateTime.now().until(estimatedRideEndTime, ChronoUnit.MINUTES);
                    if (minutesUntil >= 0 && minutesUntil <= Constants.vehicleWaitTimeInMinutes) {
                        currentlyUnavailablePotentialDrivers.add(driver);
                    }
                }
                continue;
            }

            if (availablePotentialDriver == null) {
                availablePotentialDriver = driver;
                distanceFromStartLocationAvailableDriver = mapService.getDistance(rideStartLocation.getLatitude(), rideStartLocation.getLongitude(), availablePotentialDriver.getVehicle().getLocation().getLatitude(), availablePotentialDriver.getVehicle().getLocation().getLongitude());
            } else {
                // picking the available driver who is closer to the start point of the ride
                Integer nextPotentialDriverDistance = mapService.getDistance(rideStartLocation.getLatitude(), rideStartLocation.getLongitude(), driver.getVehicle().getLocation().getLatitude(), driver.getVehicle().getLocation().getLongitude());
                if (nextPotentialDriverDistance < distanceFromStartLocationAvailableDriver) {
                    availablePotentialDriver = driver;
                }
            }

        }

        // priority is given to the driver that is currently available
        if (availablePotentialDriver != null) {
            rideToSchedule.setDriver(availablePotentialDriver);
        } else {
            Driver driver = currentlyUnavailablePotentialDrivers.stream().max(Comparator.comparing((Driver d) -> mapService.getDistance(rideStartLocation.getLatitude(), rideStartLocation.getLongitude(), d.getVehicle().getLocation().getLatitude(), d.getVehicle().getLocation().getLongitude()))).orElse(null);

            if (driver == null) {
                throw new DriverNotFoundException("There are no available drivers at that moment");
            }

            rideToSchedule.setDriver(driver);

            // if the driver is initially busy then the start ride is moved by 5 minutes
            rideToSchedule.setStartTime(rideToSchedule.getStartTime().plusMinutes(Constants.vehicleWaitTimeInMinutes));
        }

        // setting vehicle type for the ride
        rideToSchedule.setVehicleType(vehicleTypeService.getByName(rideCreationDto.getVehicleType()));

        // setting the price
        int totalDistance = 0;
        for (Route r : rideToSchedule.getRoutes()) {
            totalDistance += r.getDistanceInMeters();
        }
        rideToSchedule.setPrice(Math.round(rideToSchedule.getVehicleType().getPricePerKm() + totalDistance / 1000 * 120));

        // adding passengers to ride
        for (UserRefDto passengerRef : rideCreationDto.getPassengers()) {
            Passenger passenger = passengerService.findById(passengerRef.getId());
            if (passenger == null) {
                continue;
            }
            passenger.getRides().add(rideToSchedule);
            rideToSchedule.getPassengers().add(passenger);
        }

        // scheduling the ride by sending it to the database
        save(rideToSchedule);
        return new RideDto(rideToSchedule);
    }

    //Milos
    public Set<Passenger> addPassengerFromFavoriteLocation(FavoriteLocationDto favoriteLocationDto) throws UserNotFoundException {
        if(favoriteLocationDto == null)
            return new HashSet<>();
        Set<Passenger>passengers = new HashSet<>();
        for (UserRefDto pass : favoriteLocationDto.getPassengers()) {
            Passenger passenger = passengerService.findById(pass.getId());
            if (passenger == null) throw new UserNotFoundException();
            passengers.add(passenger);
        }
        return passengers;
    }

    // Testirano
    public List<RideDto> getAllFinishedRides(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Collection<RideDto> rides = new ArrayList<>();

        if (user instanceof Driver driver) {
            rideRepository.findRidesByDriverId(driver.getId()).forEach(ride -> rides.add(new RideDto(ride)));
        } else if (user instanceof Passenger passenger) {
            rideRepository.findRidesByPassengersId(passenger.getId()).forEach(ride -> rides.add(new RideDto(ride)));
        } else {
            throw new UserNotFoundException("Only driver or passenger can have rides!");
        }

        List<RideDto> finishedRides = new ArrayList<>();
        for (RideDto ride : rides) {
            if (ride.getStatus().equals("FINISHED")) {
                finishedRides.add(ride);
            }
        }

        return finishedRides;
    }


    // Testirano
    public List<RideDto> getAllRejectedRides(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Collection<RideDto> rides = new ArrayList<>();

        if (user instanceof Driver driver) {
            rideRepository.findRidesByDriverId(driver.getId()).forEach(ride -> rides.add(new RideDto(ride)));
        } else if (user instanceof Passenger passenger) {
            rideRepository.findRidesByPassengersId(passenger.getId()).forEach(ride -> rides.add(new RideDto(ride)));
        } else {
            throw new UserNotFoundException("Only driver or passenger can have rides!");
        }

        List<RideDto> rejectedRides = new ArrayList<>();
        for (RideDto ride : rides) {
            if (ride.getStatus().equals("REJECTED")) {
                rejectedRides.add(ride);
            }
        }

        return rejectedRides;
    }


    // Testirano
    public PaginatedResponseDto<RideDto> getPaginatedRidesForDriverAsDto(Long driverId, Pageable page) throws DriverNotFoundException {
        driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);
        List<RideDto> rides = rideRepository.findRidesByDriverId(driverId).stream().map(RideDto::new).toList();
        return new PaginatedResponseDto<>(rides.size(), rides);
    }


    // Testirano
    public Collection<RideDto> getScheduledRidesForDriverAsDto(Long driverId) throws DriverNotFoundException, RideNotFoundException {
        driverRepository.findById(driverId).orElseThrow(DriverNotFoundException::new);

        List<Ride> pendingRides = rideRepository.findByDriverIdAndStatus(driverId, Enums.RideStatus.PENDING.ordinal()).orElseThrow(RideNotFoundException::new);
        Collection<RideDto> pendingRidesDto = pendingRides.stream().map(RideDto::new).toList();


        List<Ride> acceptedRides = rideRepository.findByDriverIdAndStatus(driverId, Enums.RideStatus.ACCEPTED.ordinal()).orElseThrow(RideNotFoundException::new);
        Collection<RideDto> acceptedRidesDto = acceptedRides.stream().map((Ride ride) -> {
            ride.setRoutes(ride.getRoutes().stream().sorted(Comparator.comparing(Route::getId)).toList());
            return new RideDto(ride);
        }).toList();

        Collection<RideDto> rides = new ArrayList<>();
        rides.addAll(pendingRidesDto);
        rides.addAll(acceptedRidesDto);
        return rides.stream().sorted(Comparator.comparing((RideDto ride) -> LocalDateTime.parse(ride.getStartTime(), Constants.customDateTimeFormat))).toList();
    }

}
