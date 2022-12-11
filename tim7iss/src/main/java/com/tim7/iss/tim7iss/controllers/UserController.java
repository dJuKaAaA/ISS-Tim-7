package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.*;
import com.tim7.iss.tim7iss.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;


    @GetMapping("/api/user/{id}/ride")
    public ResponseEntity<RidesDTO> getRides(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("get rides");
//        return userService.getRides(id);
        return new ResponseEntity<>(userService.getRidesK1(), HttpStatus.OK);
    }

    @GetMapping("/api/user")
    public ResponseEntity<UsersDTO> getUserDetails() {
        LOGGER.info("get user details");
//        return userService.getUsersDetails();
        return new ResponseEntity<>(userService.getUsersDetailsK1(), HttpStatus.OK);
    }

    @PostMapping("/api/user/login")
    public ResponseEntity<POSTLoginDTO> login(@RequestBody LoginDTO loginDTO) {
        LOGGER.info("login");
//        userService.login(loginDTO);
        return new ResponseEntity<>(userService.loginK1(),HttpStatus.OK);
    }

    @GetMapping("/api/user/{id}/message")
    public ResponseEntity<MessagesDTO> getMessages(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("get messages");
//        return userService.getMessages(id);
        return new ResponseEntity<>(userService.getMessagesK1(),HttpStatus.OK);
    }

    @PostMapping("/api/user/{id}/message")
    public ResponseEntity<MessageDTO> sendMessage(@PathVariable("id") Long id,
                                                  @RequestBody POSTMessageDTO messageDTO) throws Exception {
        LOGGER.info("send messages");
//        return userService.sendMessage(id,messageDTO);
        return new ResponseEntity<>(userService.sendMessageK1(),HttpStatus.OK);
    }

    @PutMapping("/api/user/{id}/block")
    public ResponseEntity block(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("block");
//        return userService.block(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/api/user/{id}/unblock")
    public ResponseEntity unblock(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("unblock");
//        return userService.unblock(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Add note for user to help to decide to ban user
    @PostMapping("/api/user/{id}/note")
    public ResponseEntity<NoteDTO> addNoteForUser(@PathVariable("id") Long userId,
                                                 @RequestBody SimpleMessageDTO noteDTO) throws Exception {
        LOGGER.info("create note");
//        return userService.addNote(userId,noteDTO);
        return new ResponseEntity<>(userService.addNoteK1(), HttpStatus.OK);

    }

    // Get note for user to help to decide to ban user
    @GetMapping("/api/user/{id}/note")
    public ResponseEntity<NotesDTO> getNotes(@PathVariable("id") Long userId) throws Exception {
        LOGGER.info("get notes");
//        return userService.getNotes(userId);
        return new ResponseEntity<>(userService.getNotesK1(),HttpStatus.OK);
    }


}
