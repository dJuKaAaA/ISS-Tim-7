package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.DTOs.*;
import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {


    @Autowired
    DriverRepository driverRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    RideRepository rideRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<PaginatedResponseDto<RideDto>> getRides(Long id) throws Exception {
        User user = userRepository.findById(id).orElse(null);

        if (user instanceof Driver driver) {
            List<RideDto> rides = new ArrayList<>();
            rideRepository.findRidesByDriverId(driver.getId()).forEach(ride -> rides.add(new RideDto(ride)));
            return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);

        } else if (user instanceof Passenger passenger) {
            List<RideDto> rides = new ArrayList<>();
            rideRepository.findRidesByPassengersId(passenger.getId()).forEach(ride -> rides.add(new RideDto(ride)));
            return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);

        }
        return null;

    }

    public RidesDTO getRidesK1() {

        RidesDTO retVal = new RidesDTO();
        retVal.setTotalCount(2);

        RideDTO ride = new RideDTO();
        ride.setId(1L);

        LocationDTO location = new LocationDTO("adresa", 45.2121, 43.416546);

        HashSet<RouteDTO> route = new HashSet<>();
        RouteDTO routeDTO = new RouteDTO(location, location);
        route.add(routeDTO);

        ride.setLocations(route);
        ride.setStartTime(LocalDateTime.now().format(Constants.customDateTimeFormat));
        ride.setEndTime(LocalDateTime.now().format(Constants.customDateTimeFormat));
        ride.setTotalCost(123);
        ride.setDriver(new SimpleDriverDTO(1L, "email"));
        Set<SimplePassengerDTO> passengers = new HashSet<>();
        SimplePassengerDTO passenger = new SimplePassengerDTO(1L, "ivan");
        passengers.add(passenger);
        ride.setPassengers(passengers);
        ride.setEstimatedTimeInMinutes(5);
        ride.setVehicleType("STANDARD");
        ride.setBabyTransport(true);
        ride.setPetTransport(false);
        RejectionDTO rejectionDTO = new RejectionDTO("reason", LocalDateTime.now().format(Constants.customDateTimeFormat));
        ride.setRejection(rejectionDTO);

        RideDTO ride2 = getRide();
        ride2.setStartTime("01.01.2022 16:40:30");
        Set<RideDTO> rides = new HashSet<>();
        rides.add(ride);
        rides.add(ride2);
        retVal.setResults(rides);
        return retVal;
    }

    // DELETE
    private RideDTO getRide() {

        RideDTO ride = new RideDTO();
        ride.setId(1L);

        LocationDTO location = new LocationDTO("adresa", 45.2121, 43.416546);

        HashSet<RouteDTO> route = new HashSet<>();
        RouteDTO routeDTO = new RouteDTO(location, location);
        route.add(routeDTO);

        ride.setLocations(route);
        ride.setStartTime(LocalDateTime.now().format(Constants.customDateTimeFormat));
        ride.setEndTime(LocalDateTime.now().format(Constants.customDateTimeFormat));
        ride.setTotalCost(123);
        ride.setDriver(new SimpleDriverDTO(1L, "email"));
        Set<SimplePassengerDTO> passengers = new HashSet<>();
        SimplePassengerDTO passenger = new SimplePassengerDTO(1L, "ivan");
        passengers.add(passenger);
        ride.setPassengers(passengers);
        ride.setEstimatedTimeInMinutes(5);
        ride.setVehicleType("STANDARD");
        ride.setBabyTransport(true);
        ride.setPetTransport(false);
        RejectionDTO rejectionDTO = new RejectionDTO("reason", LocalDateTime.now().format(Constants.customDateTimeFormat));
        ride.setRejection(rejectionDTO);
        return ride;
    }

    public ResponseEntity<PaginatedResponseDto<UserDto>> getUsersDetails() {
        Collection<UserDto> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(new UserDto(user)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(users.size(), users),HttpStatus.OK);
    }

    public UsersDTO getUsersDetailsK1() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("Pera");
        userDTO.setSurname("Peric");
        userDTO.setProfilePicture("Profile picture");
        userDTO.setTelephoneNumber("0604672999");
        userDTO.setEmail("email");
        userDTO.setAddress("address");

        Set<UserDTO> userDTOS = new HashSet<>();
        userDTOS.add(userDTO);

        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setTotalCount(1);
        usersDTO.setResults(userDTOS);
        return usersDTO;
    }

    public ResponseEntity<TokenResponseDto> login(LoginDto loginDto) {
        return new ResponseEntity<>(new TokenResponseDto("neki", "token"), HttpStatus.OK);
    }

    public POSTLoginDTO loginK1() {
        POSTLoginDTO postLoginDTO = new POSTLoginDTO();
        postLoginDTO.setAccessToken("access token");
        postLoginDTO.setRefreshToken("refresh token");
        return postLoginDTO;
    }

    public ResponseEntity<PaginatedResponseDto<MessageDto>> getMessages(Long id) throws Exception {

        User user = userRepository.findById(id).orElse(null);
        assert user != null;
        Collection<MessageDto> messages = new ArrayList<>();
        getAllMessages(user).forEach(message -> messages.add(new MessageDto(message)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(messages.size(), messages), HttpStatus.OK);

    }

    public MessagesDTO getMessagesK1() {
        Set<MessageDTO> messageDTOS = new HashSet<>();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setTimeOfSending(LocalDateTime.now().format(Constants.customDateTimeFormat));
        messageDTO.setSenderId(1L);
        messageDTO.setReceiverId(2L);
        messageDTO.setMessage("message");
        messageDTO.setType("RIDE");
        messageDTO.setRideId(1L);
        messageDTOS.add(messageDTO);
        MessagesDTO messagesDTO = new MessagesDTO(1, messageDTOS);
        return messagesDTO;
    }


    public ResponseEntity<MessageDto> sendMessage(Long id, MessageDto messageDto) {

        Ride ride = rideRepository.findById(messageDto.getRideId()).get();
        User receiver = userRepository.findById(messageDto.getReceiverId()).get();
        User sender = userRepository.findById(id).get();

        Message message = new Message(messageDto, ride, sender, receiver);
        messageRepository.save(message);
        return new ResponseEntity<>(new MessageDto(message), HttpStatus.OK);

    }

    public MessageDTO sendMessageK1() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setTimeOfSending(LocalDateTime.now().format(Constants.customDateTimeFormat));
        messageDTO.setSenderId(1L);
        messageDTO.setReceiverId(1L);
        messageDTO.setMessage("message");
        messageDTO.setType("RIDE");
        messageDTO.setRideId(1L);
        return messageDTO;
    }

    public ResponseEntity block(Long id) {
        User user = userRepository.findById(id).get();
        changeUserBlockedState(user, true);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity blockK1() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity unblock(Long id) throws Exception {
        User user = userRepository.findById(id).get();
        changeUserBlockedState(user, false);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    public ResponseEntity unblockK1() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


    public ResponseEntity<NoteDto> addNote(Long userId, NoteDto postNoteDto) {
        User user = userRepository.findById(userId).orElse(null);
        Note note = Note.builder().date(LocalDateTime.now()).user(user).message(postNoteDto.getMessage()).build();
        noteRepository.save(note);
        return new ResponseEntity<>(new NoteDto(note), HttpStatus.OK);

    }

    public NoteDTO addNoteK1() {
        NoteDTO noteDTO = new NoteDTO(1L, LocalDateTime.now().format(Constants.customDateTimeFormat), "message");
        return noteDTO;
    }

    public ResponseEntity<PaginatedResponseDto<NoteDto>> getNotes(Long userId) {
        Collection<NoteDto> notes = new ArrayList<>();
        noteRepository.findAllByUserId(userId).forEach(note -> notes.add(new NoteDto(note)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(notes.size(), notes), HttpStatus.OK);
    }

    public NotesDTO getNotesK1() {
        NoteDTO noteDTO = new NoteDTO(1L, LocalDateTime.now().format(Constants.customDateTimeFormat), "message");
        Set<NoteDTO> noteDTOS = new HashSet<>();
        noteDTOS.add(noteDTO);

        NotesDTO notesDTO = new NotesDTO();
        notesDTO.setTotalCount(1);
        notesDTO.setResults(noteDTOS);
        return notesDTO;
    }

    private List<Message> getAllMessages(User user) {
        List<Message> sentMessages = messageRepository.findAllBySenderId(user.getId());
        List<Message> receivedMessages = messageRepository.findAllByReceiverId(user.getId());

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(receivedMessages);
        return allMessages;
    }

    private User getUser(Long userId) throws Exception {

        Optional<Driver> driverOptional = driverRepository.findById(userId);
        Optional<Passenger> passengerOptional = passengerRepository.findById(userId);
        Optional<Admin> adminOptional = adminRepository.findById(userId);

        if (driverOptional.isEmpty() && passengerOptional.isEmpty() && adminOptional.isEmpty()) {
            throw new Exception("User not found");
        } else if (driverOptional.isPresent()) {
            return driverOptional.get();
        } else if (passengerOptional.isPresent()) {
            return passengerOptional.get();
        } else {
            return adminOptional.get();
        }
    }

    private void changeUserBlockedState(User user, boolean blockedState) {
        if (user instanceof Passenger passenger) {
            passenger.setBlocked(blockedState);
            passengerRepository.save(passenger);
        } else if (user instanceof Driver driver) {
            driver.setBlocked(blockedState);
            driverRepository.save(driver);
        }
    }


}
