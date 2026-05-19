package com.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
public class ConversationDTO {
    private Long id;
    private String subject;
    private String sender;
    private String lastMessage;
    private String lastAt;
    private int unreadCount;
    private List<MessageDTO> messages;
}