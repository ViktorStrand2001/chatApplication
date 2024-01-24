package com.viktor.chatApplication.controllers.UI;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/chat")
    public String index(){
        return "chat";
    }

}
