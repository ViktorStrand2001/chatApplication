package com.viktor.chatApplication.controllers.adminController;

import com.viktor.chatApplication.models.UserModel;
import com.viktor.chatApplication.services.MessageService;
import com.viktor.chatApplication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public AdminController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/admin-page")
    public String registerUserPage(
            Model model,
            UserModel userModel
    ){

        List<UserModel> allUsers = userService.getAllUsers();
        List<UserModel> users = allUsers.stream().toList();

        model.addAttribute("userList", users);

        return "admin-page";
    }

    @PreAuthorize("hasAuthority('ADMINGET') and hasAuthority('ADMINPOST') and hasAuthority('ADMINPUT') and hasAuthority('ADMINDELETE')")
    @RequestMapping(value = "/admin-page", method = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
    public String adminHandler(
            UserModel userModel,
            Model model,
            @RequestParam(value = "action", required = false)
            String action
            ) throws Exception{
        try {
            switch (action){
                case "delete" -> {
                    Optional<UserModel> user = userService.getById(userModel.getId());
                    userService.deleteUser(user);
                }
                case "put" -> {
                    userService.updateUser(userModel.getId(), userModel);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/admin-page";
    }

}
