package com.viktor.chatApplication.controllers.userControllers;

import com.viktor.chatApplication.enums.Roles;
import com.viktor.chatApplication.models.UserModel;
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

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String registerUserPage(UserModel userModel, Model model){

        model.addAttribute("roles", Roles.values());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid UserModel newUser,
            BindingResult bindingResult,
            Model model
    ){

        if (bindingResult.hasErrors()){
            return "register";
        }

        if (userService.doesUsernameExist(newUser.getUsername())) {
            bindingResult.rejectValue("username", "error.userModel", "Username already exists");
            return "register";
        }

        userService.createUser(newUser);

        return "redirect:/login";
    }

}

