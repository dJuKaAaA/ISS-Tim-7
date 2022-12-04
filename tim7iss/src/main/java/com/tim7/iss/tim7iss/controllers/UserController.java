package com.tim7.iss.tim7iss.controllers;

import com.tim7.iss.tim7iss.DTOs.*;
import com.tim7.iss.tim7iss.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Transactional
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;


    @GetMapping("/api/user/{id}/ride")
    public RidesDTO getRides(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("get rides");
        return userService.getRides(id);
    }

    @GetMapping("/api/user")
    public UsersDTO getUserDetails() {
        LOGGER.info("get user details");
        return userService.getUsersDetails();
    }

    @PostMapping("/api/login")
    public String login(@RequestBody LoginDTO loginDTO) {
        LOGGER.info("login");
        userService.login(loginDTO);
        return "login";
    }

    @GetMapping("/api/user/{id}/message")
    public MessagesDTO getMessages(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("get messages");
        return userService.getMessages(id);
    }

    @PostMapping("/api/user/{id}/message")
    public MessageDTO sendMessage(@PathVariable("id") Long id, @RequestBody POSTMessageDTO messageDTO) throws Exception {
        LOGGER.info("send messages");
        return userService.sendMessage(id,messageDTO);
    }

    @PutMapping("/api/user/{id}/block")
    public String block(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("block");
        return userService.block(id);
    }

    @PutMapping("/api/user/{id}/unblock")
    public String unblock(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("unblock");
        return userService.unblock(id);
    }

    // Add note for user to help to decide to ban user
    @PostMapping("/api/user/{id}/note")
    public NoteDTO addNotificationForUser(@PathVariable("id") Long userId, @RequestBody POSTNoteDTO notificationDTO) throws Exception {
        LOGGER.info("create note");
        return userService.addNote(userId,notificationDTO);
    }

    // Get note for user to help to decide to ban user
    @GetMapping("/api/user/{id}/note")
    public NotesDTO getNotes(@PathVariable("id") Long userId) throws Exception {
        LOGGER.info("get notes");
        return userService.getNotes(userId);
    }

}
