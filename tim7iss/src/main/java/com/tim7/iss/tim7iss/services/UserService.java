package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.DTOs.*;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    public RidesDTO getRides(Long id) throws Exception {
        User user = userRepository.findById(id).get();

        if (user instanceof Driver driver) {
            List<Ride> rides = rideRepository.findRidesByDriverId(driver.getId());
            return new RidesDTO(new HashSet<>(rides));

        } else if (user instanceof Passenger passenger) {
            List<Ride> rides = rideRepository.findRidesByPassengersId(passenger.getId());
            return new RidesDTO(new HashSet<>(rides));

        }
        return null;

    }

    public RidesDTO getRidesK1() {

        RidesDTO retVal = new RidesDTO();
        retVal.setTotalCount(1);

        RideDTO ride = new RideDTO();
        ride.setId(1L);

        LocationDTO location = new LocationDTO("adresa", 45.2121, 43.416546);

        HashSet<RouteDTO> route = new HashSet<>();
        RouteDTO routeDTO = new RouteDTO(location, location);
        route.add(routeDTO);

        ride.setLocations(route);
        ride.setStartTime(LocalDateTime.now().toString());
        ride.setEndTime(LocalDateTime.now().toString());
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
        RejectionDTO rejectionDTO = new RejectionDTO("reason", LocalDateTime.now().toString());
        ride.setRejection(rejectionDTO);

        Set<RideDTO> rides = new HashSet<>();
        rides.add(ride);
        retVal.setResults(rides);
        return retVal;
    }

    public UsersDTO getUsersDetails() {
        Set<User> users = new HashSet<>(userRepository.findAll());
        return new UsersDTO(users);
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

    public POSTLoginDTO login(LoginDTO loginDTO) {
        return null;
    }

    public POSTLoginDTO loginK1() {
        POSTLoginDTO postLoginDTO = new POSTLoginDTO();
        postLoginDTO.setAccessToken("access token");
        postLoginDTO.setRefreshToken("refresh token");
        return postLoginDTO;
    }

    public MessagesDTO getMessages(Long id) throws Exception {

        User user = userRepository.findById(id).get();
        Set<Message> messages = new HashSet<>(getAllMessages(user));
        return new MessagesDTO(messages);

    }

    public MessagesDTO getMessagesK1() {
        Set<MessageDTO> messageDTOS = new HashSet<>();
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setTimeOfSending(LocalDateTime.now().toString());
        messageDTO.setSenderId(1L);
        messageDTO.setReceiverId(2L);
        messageDTO.setMessage("message");
        messageDTO.setType("RIDE");
        messageDTO.setRideId(1L);
        messageDTOS.add(messageDTO);
        MessagesDTO messagesDTO = new MessagesDTO(1, messageDTOS);
        return messagesDTO;
    }


    public MessageDTO sendMessage(Long id, POSTMessageDTO messageDTO) throws Exception {

        Ride ride = rideRepository.findById(messageDTO.getRideId()).get();
        User receiver = userRepository.findById(messageDTO.getReceiverId()).get();
        User sender = userRepository.findById(id).get();

        Message message = messageDTO.getMessage();
        message.setRide(ride);
        message.setSender(sender);
        message.setReceiver(receiver);
        messageRepository.save(message);
        return new MessageDTO(message);

    }

    public MessageDTO sendMessageK1() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setTimeOfSending(LocalDateTime.now().toString());
        messageDTO.setSenderId(1L);
        messageDTO.setReceiverId(1L);
        messageDTO.setMessage("message");
        messageDTO.setType("RIDE");
        messageDTO.setRideId(1L);
        return messageDTO;
    }

    public SimpleMessageDTO block(Long id) throws Exception {
        User user = userRepository.findById(id).get();
        changeUserBlockedState(user, true);
        return new SimpleMessageDTO("User is successfully blocked");
    }

    public SimpleMessageDTO blockK1() {
        return new SimpleMessageDTO("User is successfully blocked");
    }

    public SimpleMessageDTO unblock(Long id) throws Exception {
        User user = userRepository.findById(id).get();
        changeUserBlockedState(user, false);
        return new SimpleMessageDTO("User is successfully unblocked");

    }

    public SimpleMessageDTO unblockK1() {
        return new SimpleMessageDTO("User is successfully unblocked");

    }


    public NoteDTO addNote(Long userId, SimpleMessageDTO postNoteDTO) throws Exception {
        User user = userRepository.findById(userId).get();
        Note note = Note.builder().date(LocalDateTime.now()).user(user).message(postNoteDTO.getMessage()).build();
        noteRepository.save(note);
        return new NoteDTO(note);

    }

    public NoteDTO addNoteK1() {
        NoteDTO noteDTO = new NoteDTO(1L, LocalDateTime.now().toString(), "message");
        return noteDTO;
    }

    public NotesDTO getNotes(Long userId) throws Exception {
        List<Note> notes = noteRepository.findAllByUserId(userId);
        return new NotesDTO(new HashSet<>(notes));
    }

    public NotesDTO getNotesK1() {
        NoteDTO noteDTO = new NoteDTO(1L, LocalDateTime.now().toString(), "message");
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
