package com.viktor.chatApplication.controllers.messageControllers;

import com.viktor.chatApplication.models.MessageModel;
import com.viktor.chatApplication.models.UserModel;
import com.viktor.chatApplication.services.MessageService;
import com.viktor.chatApplication.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MessageController {

    private final UserService userService;
    private final MessageService messageService;
    @Autowired
    public MessageController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/chat")
    public String chaty(Model model, MessageModel messageModel){

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

    @PreAuthorize("hasAuthority('USERGET') and hasAuthority('USERPOST') and hasAuthority('USERPUT') and hasAuthority('USERDELETE')")
    @RequestMapping(value = "/chat", method = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
    public String handleMessageActions(
            @Valid MessageModel messageModel,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            @RequestParam(value = "action", required = false)
            String action) throws Exception {

        switch(action){

            case "send" -> {
                if (bindingResult.hasErrors()) {
                    return "chat";
                }
                messageService.sendMessage(messageModel, authentication);
            }

            case "delete" -> {
                Optional<MessageModel> message = messageService.getById(messageModel.getId());
                messageService.deleteMessage(message);
            }

            case "put" ->{
                Optional<MessageModel> existingMessage = messageService.getById(messageModel.getId());
                if (existingMessage.isPresent()) {
                    MessageModel editedMessage = existingMessage.get();
                    editedMessage.setContent(messageModel.getContent());

                    messageService.editMessage(editedMessage.getId(), editedMessage);
                }
            }
            default -> System.out.println("Nothing happened!");
        }
        return "redirect:/chat";
    }
}