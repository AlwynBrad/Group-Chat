package com.alwyn.groupchat.config;

import com.alwyn.groupchat.dto.ChatMessageDTO;
import com.alwyn.groupchat.enums.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListner {

        private  final SimpMessageSendingOperations messageSendingOperations;

        @EventListener
        public void handleWebSocketDisconnectListner(SessionDisconnectEvent event){
                StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
                String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
                if (username != null){
                        var chatMessageDTO = ChatMessageDTO.builder()
                                .type(MessageType.LEAVE)
                                .Sender(username)
                                .build();
                        messageSendingOperations.convertAndSend("/topic/public",chatMessageDTO);
                }
        }
}
