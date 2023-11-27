package com.alwyn.groupchat.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alwyn.groupchat.dto.ChatMessageDTO;
import com.alwyn.groupchat.enums.MessageType;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ChatController.class})
@ExtendWith(SpringExtension.class)
class ChatControllerTest {
  @Autowired
  private ChatController chatController;

  /**
   * Test for verifying the behavior of the sendMessage method.
   * Method under test: {@link ChatController#sendMessage(ChatMessageDTO)}
   */
  @Test
  void testSendMessage() {
    ChatMessageDTO chatMessageDTO = new ChatMessageDTO("Sender", "Not all who wander are lost", MessageType.CHAT);

    assertSame(chatMessageDTO, chatController.sendMessage(chatMessageDTO));
  }

  /**
     * Test for verifying the behavior of the sendMessage method with a mocked ChatMessageDTO.
     * Method under test: {@link ChatController#sendMessage(ChatMessageDTO)}
     */
  @Test
  void testSendMessage2() {
    ChatMessageDTO chatMessageDTO = mock(ChatMessageDTO.class);
    assertSame(chatMessageDTO, chatController.sendMessage(chatMessageDTO));
  }


  /**
   * Test for verifying the behavior of the userJoined method.
   * Method under test:
   * {@link ChatController#userJoined(ChatMessageDTO, SimpMessageHeaderAccessor)}
   */
  @Test
  void testUserJoined() {
    ChatMessageDTO chatMessageDTO = new ChatMessageDTO("Sender", "Not all who wander are lost", MessageType.CHAT);

    SimpMessageHeaderAccessor headerAccessor = mock(SimpMessageHeaderAccessor.class);
    when(headerAccessor.getSessionAttributes()).thenReturn(new HashMap<>());
    ChatMessageDTO actualUserJoinedResult = chatController.userJoined(chatMessageDTO, headerAccessor);
    verify(headerAccessor).getSessionAttributes();
    assertSame(chatMessageDTO, actualUserJoinedResult);
  }
}
