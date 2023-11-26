package com.alwyn.groupchat.controller;

import com.alwyn.groupchat.dto.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO chatMessageDTO){
        return  chatMessageDTO;
    }

    @MessageMapping("/chat.userJoined")
    @SendTo("/topic/public")
    public ChatMessageDTO userJoined(
            @Payload ChatMessageDTO chatMessageDTO,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatMessageDTO.getSender());
        return  chatMessageDTO;
    }

}
