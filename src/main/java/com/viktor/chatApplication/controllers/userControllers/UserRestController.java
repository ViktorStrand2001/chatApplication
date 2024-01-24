package com.viktor.chatApplication.controllers.userControllers;

import com.viktor.chatApplication.config.AppPasswordConfig;
import com.viktor.chatApplication.models.UserModel;
import com.viktor.chatApplication.repositories.IUserRepository;
import com.viktor.chatApplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;
    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<UserModel> createNewUser(@RequestBody UserModel newUser){

        userService.createUser(newUser);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('GET')")
    public ResponseEntity<String> checkGetAuthority(){
        return new ResponseEntity<>("You can only enter with GET Authority!", HttpStatus.ACCEPTED);
    }
}

