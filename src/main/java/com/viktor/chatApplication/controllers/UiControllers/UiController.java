package com.viktor.chatApplication.controllers.UiControllers;

import com.viktor.chatApplication.models.UserModel;
import com.viktor.chatApplication.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UiController {

    private final UserService userService;

    public UiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/chat")
    public String chaty(Model model){

        // Låter spring Security hantera "currentUser" alltså den som är authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        List<UserModel> allUsers = userService.getAllUsers();

        List<UserModel> otherUsers = allUsers.stream()
                .filter(user -> !user.getUsername().equals(currentUsername)).toList();


        model.addAttribute("currentUser", currentUsername);
        model.addAttribute("allOnlineUsers", otherUsers);

        return "chat";
    }
}
