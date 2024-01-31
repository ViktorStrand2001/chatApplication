package com.viktor.chatApplication.controllers.messageControllers;

import com.viktor.chatApplication.models.MessageModel;
import com.viktor.chatApplication.models.UserModel;
import com.viktor.chatApplication.services.MessageService;
import com.viktor.chatApplication.services.UserService;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private final UserService userService;
    private final MessageService messageService;

    public MessageController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    ///////////////////////   METHODS   //////////////////////////


    ////////   chat    //////////
    @GetMapping("/chat")
    public String chaty(Model model, MessageModel messageModel){

        // Låter spring Security hantera "currentUser" alltså den som är authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        List<UserModel> allUsers = userService.getAllUsers();
        List<UserModel> otherUsers = allUsers.stream()
                .filter(user -> !user.getUsername().equals(currentUsername)).toList();

        List<MessageModel> messages = messageService.massageHistory();

        model.addAttribute("messageHistory", messages);
        model.addAttribute("currentUser", currentUsername);
        model.addAttribute("allUsers", otherUsers);

        return "chat";
    }

    ////////   Post message    //////////
    @PostMapping("/chat")
    public String postMessage(
            @Valid MessageModel messageModel,
            BindingResult bindingResult,
            Authentication authentication,
            Model model
    ) throws Exception {
        if (bindingResult.hasErrors()){
            return "chat";
        }

        messageService.sendMessage(messageModel, authentication);

        // send(messageModel); ???

        return "redirect:/chat";
    }

    /////////// PUT ///////////


    /////////// Delete ///////////


    ////////   WEBSOCKET    //////////
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public MessageModel send(MessageModel message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new MessageModel(message.getId(), message.getContent(), time);
    }
}