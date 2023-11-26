package com.alwyn.groupchat.dto;

import com.alwyn.groupchat.enums.MessageType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {

    private String Sender;
    private String Content;
    private MessageType type;
}
