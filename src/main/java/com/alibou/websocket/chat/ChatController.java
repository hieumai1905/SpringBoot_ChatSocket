package com.alibou.websocket.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatController {

    @MessageMapping("/chat.sendMessage/{room}")
    @SendTo("/topic/{room}")
    public ChatMessage sendMessage(
            @Payload ChatMessage chatMessage,
            @DestinationVariable("room") String room
    ) {
        chatMessage.setRoom(room);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser/{room}")
    @SendTo("/topic/{room}")
    public ChatMessage addUser(
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor,
            @DestinationVariable("room") String room
    ) {
        log.info("user added: {} in room {}", chatMessage.getSender(), room);
        chatMessage.setRoom(room);
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("room", room);
        return chatMessage;
    }
}