package com.service;

import com.model.Message;
import com.dto.ConversationDTO;
import com.dto.MessageDTO;
import com.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private static final String ADMIN_EMAIL = "markdoan38@gmail.com";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message saveMessage(Message message) {
        if (message.getSentAt() == null) {
            message.setSentAt(LocalDateTime.now());
        }
        return messageRepository.save(message);
    }

    public List<Message> getMessagesForParticipant(String email) {
        return messageRepository.findBySenderEmailOrReceiverEmail(email, email);
    }

    public List<Message> getMessagesByConversationId(String conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
    }

    public List<String> getDistinctConversationIds() {
        return messageRepository.findDistinctConversationIds();
    }

    public List<Message> getUnreadMessagesByReceiver(String receiverEmail) {
        return messageRepository.findByReceiverEmailAndIsReadFalse(receiverEmail);
    }

    @Transactional
    public void markMessagesAsRead(String conversationId, String receiverEmail) {
        List<Message> messages = messageRepository.findByConversationIdAndReceiverEmail(conversationId, receiverEmail);
        messages.forEach(message -> message.setRead(true));
        messageRepository.saveAll(messages);
    }

    // ========== CÁC METHOD MỚI CHO MessageController ==========

    public List<ConversationDTO> getAllConversations() {
        List<String> conversationIds = messageRepository.findDistinctConversationIds();
        List<ConversationDTO> conversations = new ArrayList<>();

        for (String convId : conversationIds) {
            List<Message> messages = messageRepository.findByConversationIdOrderBySentAtAsc(convId);
            if (!messages.isEmpty()) {
                conversations.add(buildConversationDTO(convId, messages));
            }
        }

        conversations.sort((a, b) -> b.getLastAt().compareTo(a.getLastAt()));
        return conversations;
    }

    public ConversationDTO getConversationById(String conversationId) {
        List<Message> messages = messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
        if (messages.isEmpty()) {
            return null;
        }
        return buildConversationDTO(conversationId, messages);
    }

    @Transactional
    public MessageDTO sendMessage(String conversationId, String content, boolean isFromAdmin) {
        List<Message> messages = messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);

        if (messages.isEmpty()) {
            throw new RuntimeException("Conversation not found");
        }

        Message firstMessage = messages.get(0);

        Message newMessage = new Message();
        newMessage.setConversationId(conversationId);
        newMessage.setSubject(firstMessage.getSubject());
        newMessage.setContent(content);
        newMessage.setSender(isFromAdmin ? "Admin" : firstMessage.getSender());
        newMessage.setSenderEmail(isFromAdmin ? ADMIN_EMAIL : firstMessage.getSenderEmail());
        newMessage.setReceiverEmail(isFromAdmin ? firstMessage.getSenderEmail() : ADMIN_EMAIL);
        newMessage.setRead(false);
        newMessage.setSentAt(LocalDateTime.now());

        Message saved = messageRepository.save(newMessage);
        return convertToMessageDTO(saved);
    }

    @Transactional
    public void markAsRead(String conversationId) {
        List<Message> messages = messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
        for (Message message : messages) {
            if (!message.isRead() && message.getReceiverEmail().equals(ADMIN_EMAIL)) {
                message.setRead(true);
            }
        }
        messageRepository.saveAll(messages);
    }

    public List<MessageDTO> getUnreadMessages() {
        List<Message> unreadMessages = messageRepository.findByReceiverEmailAndIsReadFalse(ADMIN_EMAIL);
        return unreadMessages.stream()
                .map(this::convertToMessageDTO)
                .collect(Collectors.toList());
    }

    // Helper methods
    private ConversationDTO buildConversationDTO(String conversationId, List<Message> messages) {
        messages.sort(Comparator.comparing(Message::getSentAt));

        Message firstMessage = messages.get(0);
        Message lastMessage = messages.get(messages.size() - 1);

        ConversationDTO dto = new ConversationDTO();
        dto.setId(Long.valueOf(conversationId.hashCode()));
        dto.setSubject(firstMessage.getSubject());
        dto.setSender(firstMessage.getSender());
        dto.setLastMessage(lastMessage.getContent());
        dto.setLastAt(formatTime(lastMessage.getSentAt()));
        dto.setUnreadCount((int) messages.stream()
                .filter(m -> !m.isRead() && m.getReceiverEmail().equals(ADMIN_EMAIL))
                .count());
        dto.setMessages(messages.stream()
                .map(this::convertToMessageDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private MessageDTO convertToMessageDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setFromMe(message.getSenderEmail().equals(ADMIN_EMAIL));
        dto.setName(message.getSender());
        dto.setTime(formatTime(message.getSentAt()));
        return dto;
    }

    private String formatTime(LocalDateTime time) {
        if (time == null) return "";
        if (time.toLocalDate().equals(LocalDateTime.now().toLocalDate())) {
            return time.format(TIME_FORMATTER);
        }
        return time.format(DATE_FORMATTER);
    }
}