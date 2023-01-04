package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.dto.*;
import com.tim7.iss.tim7iss.global.Constants;
import com.tim7.iss.tim7iss.models.User;
import com.tim7.iss.tim7iss.services.MailService;
import com.tim7.iss.tim7iss.services.UserService;
import com.tim7.iss.tim7iss.util.TokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Transactional
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

    @GetMapping("/api/user/{id}/ride")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('Passenger')")
    public ResponseEntity<PaginatedResponseDto<RideDto>> getRides(@PathVariable("id") Long id) {
        LOGGER.info("get rides");
        return userService.getRides(id);
    }

    @GetMapping("/api/user")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponseDto<UserDto>> getUserDetails() {
        LOGGER.info("get user details");
        return userService.getUsersDetails();
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginDto loginDTO) {
        LOGGER.info("login");
        //         Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
//         AuthenticationException

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), loginDTO.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new TokenResponseDto(jwt, ""));
    }

    @GetMapping("/api/user/{id}/message")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    public ResponseEntity<PaginatedResponseDto<MessageDto>> getMessages(@PathVariable("id") Long id) {
        LOGGER.info("get messages");
        return userService.getMessages(id);
    }

    @PostMapping("/api/user/{id}/message")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('DRIVER') or hasRole('PASSENGER')")
    public ResponseEntity<MessageDto> sendMessage(@PathVariable("id") Long id,
                                                  @Valid @RequestBody MessageDto messageDTO) {
        LOGGER.info("send messages");
        return userService.sendMessage(id, messageDTO);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @MessageMapping("/send/message")
    public Map<String, Object> sendMessage(String message) {
        Map<String, Object> messageConverted = Constants.parseJsonString(message);

        if (messageConverted != null) {
            if (messageConverted.containsKey("receiverId") && messageConverted.get("receiverId") != null) {
                this.simpMessagingTemplate.convertAndSend("/socket-send-message/" + messageConverted.get("receiverId"),
                        messageConverted);
                this.simpMessagingTemplate.convertAndSend("/socket-send-message/" + messageConverted.get("senderId"),
                        messageConverted);
            } else {
                this.simpMessagingTemplate.convertAndSend("/socket-send-message", messageConverted);
            }
        }
        
        return messageConverted;
    }


//    @PutMapping("/api/user/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity block(@PathVariable("id") Long id) {
        LOGGER.info("block");
        return userService.block(id);
    }

//    @PutMapping("/api/user/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity unblock(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("unblock");
        return userService.unblock(id);
    }

    // Add note for user to help to decide to ban user
//    @PostMapping("/api/user/{id}/note")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NoteDto> addNoteForUser(@PathVariable("id") Long userId,
                                                  @RequestBody NoteDto noteDTO) throws Exception {
        LOGGER.info("create note");
        return userService.addNote(userId, noteDTO);
    }

    // Get note for user to help to decide to ban user
//    @GetMapping("/api/user/{id}/note")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponseDto<NoteDto>> getNotes(@PathVariable("id") Long userId) throws Exception {
        LOGGER.info("get notes");
        return userService.getNotes(userId);
    }


}
