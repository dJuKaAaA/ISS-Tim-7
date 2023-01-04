package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    PasswordResetCodeRepository passwordResetCodeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Autowired
    MailService mailService;

    // TODO testirati
    public ResponseEntity changePassword(Long userId, ChangePasswordDto passwordDto) throws UserNotFoundException, InvalidEmailOrPasswordException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        // Check if the plaintext password and hashed password match
        if (!BCrypt.checkpw(passwordDto.oldPassword, user.getPassword())) {
            throw new InvalidEmailOrPasswordException("Current password is not matching!");
        }

        user.setPassword(BCrypt.hashpw(passwordDto.newPassword, BCrypt.gensalt()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Password successfully changed!");
    }

    // TODO testirati
    public ResponseEntity sendResetCodeToMail(Long userId) throws UserNotFoundException, IOException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String resetCode = getRandomString();

        PasswordResetCode passwordResetCode = new PasswordResetCode(resetCode, LocalDateTime.now());
        passwordResetCodeRepository.save(passwordResetCode);

        user.setPasswordResetCode(passwordResetCode);
        userRepository.save(user);

        mailService.sendTextEmail(user.getEmailAddress(), "Reset Code", resetCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Email with reset code has been sent!");
    }

    // TODO testirati
    public ResponseEntity changePasswordWithResetCode(Long userId, ResetPasswordViaCodeDto resetPasswordViaCodeDto) throws UserNotFoundException, PasswordResetCodeException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        PasswordResetCode passwordResetCode = user.getPasswordResetCode();

        if (!passwordResetCode.getCode().equals(resetPasswordViaCodeDto.code)) throw new PasswordResetCodeException();
        if (passwordResetCode.isExpired()) throw new PasswordResetCodeException();

        user.setPassword(BCrypt.hashpw(resetPasswordViaCodeDto.newPassword, BCrypt.gensalt()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Password successfully changed!");
    }

    public ResponseEntity<PaginatedResponseDto<RideDto>> getRides(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Collection<RideDto> rides = new ArrayList<>();

        if (user instanceof Driver driver) {
            rideRepository.findRidesByDriverId(driver.getId()).forEach(ride -> rides.add(new RideDto(ride)));
            return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);

        } else if (user instanceof Passenger passenger) {
            rideRepository.findRidesByPassengersId(passenger.getId()).forEach(ride -> rides.add(new RideDto(ride)));
            return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);
        }
        throw new UserNotFoundException();
    }

    public ResponseEntity<PaginatedResponseDto<UserDto>> getUsersDetails() {
        Collection<UserDto> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(new UserDto(user)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(users.size(), users), HttpStatus.OK);
    }


    public ResponseEntity<PaginatedResponseDto<MessageDto>> getMessages(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Collection<MessageDto> messages = new ArrayList<>();
        getAllMessages(user).forEach(message -> messages.add(new MessageDto(message)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(messages.size(), messages), HttpStatus.OK);

    }

    public ResponseEntity<MessageDto> sendMessage(Long id, MessageDto messageDTO) throws RideNotFoundException, UserNotFoundException {

        Ride ride = rideRepository.findById(messageDTO.getRideId()).orElseThrow(RideNotFoundException::new);
        User receiver = userRepository.findById(messageDTO.getReceiverId()).orElseThrow(() -> new UserNotFoundException("Receiver not found"));
        User sender = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Sender not found"));

        Message message = new Message(messageDTO, ride, sender, receiver);
        messageRepository.save(message);
        return new ResponseEntity<>(new MessageDto(message), HttpStatus.OK);

    }


    public ResponseEntity block(Long id) throws UserNotFoundException, UserBlockedException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (user.isBlocked()) throw new UserBlockedException("User already blocked!");
        changeUserBlockedState(user, true);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User is successfully blocked!");
    }

    public ResponseEntity unblock(Long id) throws UserNotFoundException, UserBlockedException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (!user.isBlocked()) throw new UserBlockedException("User is not blocked!");
        changeUserBlockedState(user, false);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User is successfully unblocked");
    }

    public ResponseEntity<NoteDto> addNote(Long userId, NoteDto postNoteDTO) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Note note = Note.builder().date(LocalDateTime.now()).user(user).message(postNoteDTO.getMessage()).build();
        noteRepository.save(note);
        return new ResponseEntity<>(new NoteDto(note), HttpStatus.OK);

    }

    public ResponseEntity<PaginatedResponseDto<NoteDto>> getNotes(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
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


    private void changeUserBlockedState(User user, boolean blockedState) {
        if (user instanceof Passenger passenger) {
            passenger.setBlocked(blockedState);
            passengerRepository.save(passenger);
        } else if (user instanceof Driver driver) {
            driver.setBlocked(blockedState);
            driverRepository.save(driver);
        }
    }

    private String getRandomString() {
        Random random = new Random();
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
    }


    @Override
    // Vrsimo ucitavanje korisnika
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAddress(email);
        if (user == null) {
            throw new UsernameNotFoundException("Wrong username or password!");
        } else {
            return user;
        }
    }


}
