package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.exceptions.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.Message;
import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.repositories.MessageRepository;
import com.tim7.iss.tim7iss.repositories.UserRepository;
import com.tim7.iss.tim7iss.services.MailService;
import com.tim7.iss.tim7iss.services.UserService;
import com.tim7.iss.tim7iss.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Transactional
@Validated
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @PutMapping("/api/user/{id}/changePassword")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    public ResponseEntity changePassword(@PathVariable("id") Long userId, @Valid @RequestBody ChangePasswordDto passwordDto) throws UserNotFoundException, InvalidEmailOrPasswordException {
        return userService.changePassword(userId, passwordDto);
    }

    @GetMapping("/api/user/{email}/resetPassword")
    public ResponseEntity sendResetCodeToMail(@PathVariable("email") String email) throws UserNotFoundException,
            IOException {
        return userService.sendResetCodeToMail(email);
    }

    @PutMapping("/api/user/{email}/resetPassword")
    public ResponseEntity changePasswordWithResetCode(@Valid @RequestBody ResetPasswordViaCodeDto resetPasswordViaCodeDto, @PathVariable("email") String email) throws UserNotFoundException, PasswordResetCodeException {
        return userService.changePasswordWithResetCode(email, resetPasswordViaCodeDto);
    }

    @GetMapping("/api/user/{id}/ride")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    public ResponseEntity<PaginatedResponseDto<RideDto>> getRides(@PathVariable("id") Long id,
                                                                  Pageable pageable) throws UserNotFoundException {
        LOGGER.info("get rides");
        return userService.getRides(id, pageable);
    }

    @GetMapping("/api/user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponseDto<UserDto>> getUsers(Pageable pageable) {
        LOGGER.info("get user details");
        return userService.getUsersDetails(pageable);
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginDto loginDTO) throws Exception {
        LOGGER.info("login");
        User test = userRepository.findByEmailAddress(loginDTO.getEmail());
        if (test == null) throw new Exception(new WrongPasswordOrEmailException());

        //Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();

        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new TokenResponseDto(jwt, ""));
    }

    @GetMapping("/api/user/{id}/message")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    public ResponseEntity<PaginatedResponseDto<MessageDto>> getMessages(@PathVariable("id") Long id) throws UserNotFoundException {
        LOGGER.info("get messages");
        return userService.getMessages(id);
    }

    @PostMapping("/api/user/{id}/message")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    public ResponseEntity<MessageDto> sendMessage(@RequestHeader(value = "Authorization") String authHeader,
                                                  @PathVariable("id") Long id,
                                                  @Valid @RequestBody MessageDto messageDto) throws UserNotFoundException,
            RideNotFoundException {
        LOGGER.info("send messages");
        String token = tokenUtils.getToken(authHeader);
        String senderEmail = tokenUtils.getEmailFromToken(token);
        return userService.sendMessage(id, senderEmail,messageDto);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/send/message")
    public Map<String, Object> sendMessage(String message) {
        Map<String, Object> messageConverted = Constants.parseJsonString(message);

        if (messageConverted != null) {
            if (messageConverted.containsKey("receiverId") && messageConverted.get("receiverId") != null) {
                this.simpMessagingTemplate.convertAndSend("/socket-send-message/" + messageConverted.get("receiverId"), messageConverted);
                this.simpMessagingTemplate.convertAndSend("/socket-send-message/" + messageConverted.get("senderId"), messageConverted);
            } else {
                this.simpMessagingTemplate.convertAndSend("/socket-send-message", messageConverted);
            }
        }

        return messageConverted;
    }


    @PutMapping("/api/user/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity block(@PathVariable("id") Long id) throws UserNotFoundException, UserBlockedException {
        LOGGER.info("block");
        return userService.block(id);
    }

    @PutMapping("/api/user/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity unblock(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("unblock");
        return userService.unblock(id);
    }

    // Add note for user to help to decide to ban user
    @PostMapping("/api/user/{id}/note")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NoteDto> addNoteForUser(@PathVariable("id") Long userId, @Valid @RequestBody NoteDto noteDTO) throws Exception {
        LOGGER.info("create note");
        return userService.addNote(userId, noteDTO);
    }

    // Get note for user to help to decide to ban user
    @GetMapping("/api/user/{id}/note")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponseDto<NoteDto>> getNotes(@PathVariable("id") Long userId, Pageable pageable) throws Exception {
        return userService.getNotes(userId, pageable);
    }

    @GetMapping("/api/user/{id}/messagedUsers")
    public ResponseEntity<List<UserDto>> fetchMessagedUsers(@PathVariable Long id) {
        List<User> users = userService.getAllUsersByReceivedMessages(id);
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(new UserDto(user));
        }
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping("api/user/{id}/messages")
    public ResponseEntity<List<MessageDto>> fetchLastMessages(@PathVariable Long id) {
        List<Message> messages = messageRepository.findAllByLastMessagedSent(id);
        List<MessageDto> messageDtos = new ArrayList<>();
        for (Message message : messages) {
            messageDtos.add(new MessageDto(message));
        }
        return new ResponseEntity<>(messageDtos, HttpStatus.OK);
    }


}
