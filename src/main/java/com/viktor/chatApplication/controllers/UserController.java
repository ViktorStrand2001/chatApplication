package com.viktor.chatApplication.controllers;

import com.viktor.chatApplication.config.AppPasswordConfig;
import com.viktor.chatApplication.models.UserModel;
import com.viktor.chatApplication.repositories.IUserRepository;
import com.viktor.chatApplication.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final IUserRepository iUserRepository;
    private final AppPasswordConfig appPasswordConfig;
    private final UserService userService;

    @Autowired
    public UserController(IUserRepository iUserRepository, AppPasswordConfig appPasswordConfig, UserService userService) {
        this.iUserRepository = iUserRepository;
        this.appPasswordConfig = appPasswordConfig;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerUserPage(UserModel userModel){

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid UserModel newUser,     // enables error messages
            BindingResult bindingResult,    // Ties the object with result
            Model model                     // Thymeleaf
    ){

        // Cheak for valid error
        if (bindingResult.hasErrors()){
            return "register";
        }

        // TODO - hantera dubbla data t.ex. usernames in database (helst innom model/entity)


        userService.createUser(newUser);

        return "redirect:/login"; // efter som vi använder thleaf kan man skriva register istället för register.html
    }

}

