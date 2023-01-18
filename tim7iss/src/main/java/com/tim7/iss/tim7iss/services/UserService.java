package com.tim7.iss.tim7iss.services;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.models.*;
import com.tim7.iss.tim7iss.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
    MailService mailService;

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

    public ResponseEntity sendResetCodeToMail(Long userId) throws UserNotFoundException, IOException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String resetCode = getRandomString();

        PasswordResetCode passwordResetCode = passwordResetCodeRepository.findByUserId(userId);
        if (passwordResetCode == null) {
            passwordResetCode = new PasswordResetCode();
            passwordResetCode.setUser(user);
        }
        passwordResetCode.setCode(resetCode);
        passwordResetCode.setCreationDate(LocalDateTime.now());
        passwordResetCodeRepository.save(passwordResetCode);

        mailService.sendTextEmail(user.getEmailAddress(), "Reset Code", resetCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Email with reset code has been sent!");
    }

    public ResponseEntity changePasswordWithResetCode(Long userId, ResetPasswordViaCodeDto resetPasswordViaCodeDto) throws UserNotFoundException, PasswordResetCodeException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        PasswordResetCode passwordResetCode = passwordResetCodeRepository.findByUserId(userId);

        if (passwordResetCode == null || passwordResetCode.isExpired()) throw new PasswordResetCodeException();
        if (!passwordResetCode.getCode().equals(resetPasswordViaCodeDto.code)) throw new PasswordResetCodeException();

        user.setPassword(BCrypt.hashpw(resetPasswordViaCodeDto.newPassword, BCrypt.gensalt()));
        userRepository.save(user);

        passwordResetCodeRepository.delete(passwordResetCode);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Password successfully changed!");
    }

    public ResponseEntity<PaginatedResponseDto<RideDto>> getRides(Long id, Pageable pageable) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Collection<RideDto> rides = new ArrayList<>();

        if (user instanceof Driver driver) {
            rideRepository.findRidesByDriverId(driver.getId(), pageable).forEach(ride -> rides.add(new RideDto(ride)));
            return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);

        } else if (user instanceof Passenger passenger) {
            rideRepository.findRidesByPassengersId(passenger.getId(), pageable).forEach(ride -> rides.add(new RideDto(ride)));
            return new ResponseEntity<>(new PaginatedResponseDto<>(rides.size(), rides), HttpStatus.OK);
        }
        throw new UserNotFoundException();
    }

    public ResponseEntity<PaginatedResponseDto<UserDto>> getUsersDetails(Pageable pageable) {
        Collection<UserDto> users = new ArrayList<>();
        userRepository.findAll(pageable).forEach(user -> users.add(new UserDto(user)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(users.size(), users), HttpStatus.OK);
    }


    public ResponseEntity<PaginatedResponseDto<MessageDto>> getMessages(Long id) throws UserNotFoundException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        Collection<MessageDto> messages = new ArrayList<>();
        getAllMessages(user).forEach(message -> messages.add(new MessageDto(message)));
        return new ResponseEntity<>(new PaginatedResponseDto<>(messages.size(), messages), HttpStatus.OK);

    }

    public ResponseEntity<MessageDto> sendMessage(Long id, String senderEmail, CreateMessageDto messageDTO) throws RideNotFoundException, UserNotFoundException {

        Ride ride = rideRepository.findById(messageDTO.getRideId()).orElseThrow(RideNotFoundException::new);
        User receiver = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Receiver not found"));
        User sender = userRepository.findByEmailAddress(senderEmail);
        if (sender == null) {
            throw new UserNotFoundException("Sender not found");
        }

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

    public ResponseEntity<PaginatedResponseDto<NoteDto>> getNotes(Long userId, Pageable pageable) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Collection<NoteDto> notes = new ArrayList<>();
        noteRepository.findAllByUserId(userId, pageable).forEach(note -> notes.add(new NoteDto(note)));
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

    public List<User> getAllUsersByReceivedMessages(Long id) {
        return userRepository.findByReceivedMessages(id);
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

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> findByEmailAddress(String emailAddress) {
        return Optional.of(userRepository.findByEmailAddress(emailAddress));
    }

}
