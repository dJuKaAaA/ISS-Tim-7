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

    public RidesDTO getRides(Long id) throws Exception {
        User user = getUser(id);

        if (user instanceof Driver driver) {
            List<Ride> rides = rideRepository.findRidesByDriverId(driver.getId());
            return new RidesDTO(new HashSet<>(rides));

        } else if (user instanceof Passenger passenger) {
            List<Ride> rides = rideRepository.findRidesByPassengersId(passenger.getId());
            return new RidesDTO(new HashSet<>(rides));

        }
        return null;

    }

    public UsersDTO getUsersDetails() {
        Set<User> passengers = new HashSet<>(passengerRepository.findAll());
        Set<User> drivers = new HashSet<>(driverRepository.findAll());
        Set<User> admins = new HashSet<>(adminRepository.findAll());
        Set<User> users = new HashSet<>();
        users.addAll(passengers);
        users.addAll(drivers);
        users.addAll(admins);
        return new UsersDTO(users);
    }

    public void login(LoginDTO loginDTO) {

    }

    public MessagesDTO getMessages(Long id) throws Exception {
        User user = getUser(id);


        if (user instanceof Driver driver) {
            Set<Message> messages = new HashSet<>(getAllMessages(driver));
            return new MessagesDTO(messages);
        }
        else if (user instanceof Passenger passenger) {
            Set<Message> messages = new HashSet<>(getAllMessages(passenger));
            return new MessagesDTO(messages);
        }
        else if (user instanceof Admin admin){
            Set<Message> messages = new HashSet<>(getAllMessages(admin));
            return  new MessagesDTO(messages);
        }
        return null;

    }


    public MessageDTO sendMessage(Long id, POSTMessageDTO messageDTO) throws Exception {

        Ride ride = rideRepository.findById(messageDTO.getRideId()).get();
        User receiver = getUser(messageDTO.getReceiverId());
        User sender = getUser(id);

        Message message = messageDTO.getMessage();
        message.setRide(ride);
        message.setSender(sender);
        message.setReceiver(receiver);
        messageRepository.save(message);
        return new MessageDTO(message);

    }

    public String block(Long id) throws Exception {
        User user = getUser(id);
        changeUserBlockedState(user, true);
        return "User is successfully blocked";
    }

    public String unblock(Long id) throws Exception {
        User user = getUser(id);
        changeUserBlockedState(user, false);
        return "User is successfully unblocked";
    }


    public NoteDTO addNote(Long userId, POSTNoteDTO postNoteDTO) throws Exception {
        User user = getUser(userId);
        Note note = Note.builder().date(LocalDateTime.now()).user(user).message(postNoteDTO.getMessage()).build();
        noteRepository.save(note);
        return new NoteDTO(note);

    }

    public NotesDTO getNotes(Long userId) throws Exception {
        User user = getUser(userId);
        List<Note> notes = noteRepository.findAllByUserId(userId);
        return new NotesDTO(new HashSet<>(notes));
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
