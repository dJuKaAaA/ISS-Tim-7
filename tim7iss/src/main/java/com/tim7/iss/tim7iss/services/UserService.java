package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements UserDetailsService {

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




    public ResponseEntity<PaginatedResponseDto<RideDto>> getRides(Long id)  {
        User user = userRepository.findById(id).get();

        if (user instanceof Driver driver) {
            Collection<RideDto> rides = new ArrayList<>();
            rideRepository.findRidesByDriverId(driver.getId()).forEach(ride -> rides.add(new RideDto(ride)));
            return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);

        } else if (user instanceof Passenger passenger) {
            Collection<RideDto> rides = new ArrayList<>();
            rideRepository.findRidesByPassengersId(passenger.getId()).forEach(ride -> rides.add(new RideDto(ride)));
            return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);

        }
        return null;

    }

    public ResponseEntity<PaginatedResponseDto<UserDto>> getUsersDetails() {
        Collection<UserDto> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(new UserDto(user)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(users.size(), users), HttpStatus.OK);
    }

    public ResponseEntity<TokenResponseDto> login(LoginDto loginDTO) {
        return ResponseEntity.ok(new TokenResponseDto("",""));
    }

    public ResponseEntity<PaginatedResponseDto<MessageDto>> getMessages(Long id) {
        User user = userRepository.findById(id).get();
        Collection<MessageDto> messages = new ArrayList<>();
        getAllMessages(user).forEach(message -> messages.add(new MessageDto(message)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(messages.size(), messages), HttpStatus.OK);

    }

    public ResponseEntity<MessageDto> sendMessage(Long id, MessageDto messageDTO) {

        Ride ride = rideRepository.findById(messageDTO.getRideId()).get();
        User receiver = userRepository.findById(messageDTO.getReceiverId()).get();
        User sender = userRepository.findById(id).get();

        Message message = new Message(messageDTO, ride, sender, receiver);
        messageRepository.save(message);
        return new ResponseEntity<>(new MessageDto(message), HttpStatus.OK);

    }

    public ResponseEntity block(Long id) {
        User user = userRepository.findById(id).get();
        changeUserBlockedState(user, true);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity unblock(Long id) throws Exception {
        User user = userRepository.findById(id).get();
        changeUserBlockedState(user, false);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<NoteDto> addNote(Long userId, NoteDto postNoteDTO) throws Exception {
        User user = userRepository.findById(userId).get();
        Note note = Note.builder().date(LocalDateTime.now()).user(user).message(postNoteDTO.getMessage()).build();
        noteRepository.save(note);
        return new ResponseEntity<>(new NoteDto(note), HttpStatus.OK);

    }

    public ResponseEntity<PaginatedResponseDto<NoteDto>> getNotes(Long userId) throws Exception {
        Collection<NoteDto> notes = new ArrayList<>();
        noteRepository.findAllByUserId(userId).forEach(note -> notes.add(new NoteDto(note)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(notes.size(), notes), HttpStatus.OK);
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


    @Override
    // Vrsimo ucitavanje korisnika
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAddress(email);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", email));
        } else {
            return user;
        }
    }

    public User findById(Long id){return userRepository.findById(id).orElse(null); }
}
