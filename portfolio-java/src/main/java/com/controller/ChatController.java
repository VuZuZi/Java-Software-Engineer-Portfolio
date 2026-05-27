package com.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class ChatController {

    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return message;
    }

    record ChatMessage(String sender, String content, String projectId, LocalDateTime timestamp) {
        public void setTimestamp(LocalDateTime timestamp) {
            // Lombok sẽ tự tạo
        }
    }
}