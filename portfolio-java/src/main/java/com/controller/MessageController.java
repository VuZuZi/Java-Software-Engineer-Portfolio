//package com.controller;
//
//import com.dto.ConversationDTO;
//import com.dto.MessageDTO;
//import com.service.MessageService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/messages")
//@CrossOrigin(origins = "http://localhost:4200")
//public class MessageController {
//
//    @Autowired
//    private MessageService messageService;
//
//    @GetMapping("/conversations")
//    public ResponseEntity<List<ConversationDTO>> getConversations() {
//        return ResponseEntity.ok(messageService.getAllConversations());
//    }
//
//    @GetMapping("/conversation/{conversationId}")
//    public ResponseEntity<ConversationDTO> getConversation(@PathVariable String conversationId) {
//        ConversationDTO conversation = messageService.getConversationById(conversationId);
//        if (conversation == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(conversation);
//    }
//
//    @PostMapping("/send")
//    public ResponseEntity<MessageDTO> sendMessage(@RequestBody Map<String, String> payload) {
//        String conversationId = payload.get("conversationId");
//        String content = payload.get("content");
//        boolean isFromAdmin = Boolean.parseBoolean(payload.get("isFromAdmin"));
//
//        MessageDTO message = messageService.sendMessage(conversationId, content, isFromAdmin);
//        return ResponseEntity.ok(message);
//    }
//
//    @PostMapping("/conversation/{conversationId}/read")
//    public ResponseEntity<Map<String, Boolean>> markAsRead(@PathVariable String conversationId) {
//        messageService.markAsRead(conversationId);
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("success", true);
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/unread")
//    public ResponseEntity<List<MessageDTO>> getUnreadMessages() {
//        return ResponseEntity.ok(messageService.getUnreadMessages());
//    }
//}