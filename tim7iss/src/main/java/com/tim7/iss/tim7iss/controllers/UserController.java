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
//        return userService.getRides(id);
        return userService.getRidesK1();
    }

    @GetMapping("/api/user")
    public UsersDTO getUserDetails() {
        LOGGER.info("get user details");
//        return userService.getUsersDetails();
        return userService.getUsersDetailsK1();
    }

    @PostMapping("/api/login")
    public POSTLoginDTO login(@RequestBody LoginDTO loginDTO) {
        LOGGER.info("login");
        userService.login(loginDTO);
//        return "login";
        return userService.loginK1();
    }

    @GetMapping("/api/user/{id}/message")
    public MessagesDTO getMessages(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("get messages");
//        return userService.getMessages(id);
        return userService.getMessagesK1();
    }

    @PostMapping("/api/user/{id}/message")
    public MessageDTO sendMessage(@PathVariable("id") Long id, @RequestBody POSTMessageDTO messageDTO) throws Exception {
        LOGGER.info("send messages");
//        return userService.sendMessage(id,messageDTO);
        return userService.sendMessageK1();
    }

    @PutMapping("/api/user/{id}/block")
    public SimpleMessageDTO block(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("block");
//        return userService.block(id);
        return userService.blockK1();
    }

    @PutMapping("/api/user/{id}/unblock")
    public SimpleMessageDTO unblock(@PathVariable("id") Long id) throws Exception {
        LOGGER.info("unblock");
//        return userService.unblock(id);
        return userService.unblockK1();
    }

    // Add note for user to help to decide to ban user
    @PostMapping("/api/user/{id}/note")
    public NoteDTO addNotificationForUser(@PathVariable("id") Long userId, @RequestBody SimpleMessageDTO noteDTO) throws Exception {
        LOGGER.info("create note");
//        return userService.addNote(userId,noteDTO);
        return userService.addNoteK1();
    }

    // Get note for user to help to decide to ban user
    @GetMapping("/api/user/{id}/note")
    public NotesDTO getNotes(@PathVariable("id") Long userId) throws Exception {
        LOGGER.info("get notes");
//        return userService.getNotes(userId);
        return userService.getNotesK1();
    }


}
