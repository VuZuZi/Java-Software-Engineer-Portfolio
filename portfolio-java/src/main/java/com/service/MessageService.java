package com.service;

import com.model.Message;
import com.model.MessageStatus;
import com.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service layer for message management operations.
 * Handles business logic for saving, retrieving, and managing messages.
 *
 * @author Portfolio Platform Team
 * @version 1.0
 */
@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    /**
     * Retrieves all messages exchanged between two specific users.
     * Messages are ordered by sent time ascending for proper conversation display.
     *
     * @param userEmail the email address of the first user
     * @param adminEmail the email address of the second user (admin)
     * @return list of messages sorted by sent time ascending
     */
    public List<Message> getConversationBetweenUsers(String userEmail, String adminEmail) {
        return messageRepository.findConversationBetweenUsers(userEmail, adminEmail);
    }

    /**
     * Retrieves all messages received by a specific user.
     *
     * @param receiverEmail the email address of the receiver
     * @return list of messages received by the user, ordered by sent time descending
     */
    public List<Message> getMessagesByReceiverEmail(String receiverEmail) {
        return messageRepository.findByReceiverEmailOrderBySentAtDesc(receiverEmail);
    }

    /**
     * Retrieves all messages sent by a specific user.
     *
     * @param email the email address of the user
     * @return list of messages sent by the user, ordered by sent time descending
     */
    public List<Message> getMessagesByEmail(String email) {
        return messageRepository.findByEmailOrderBySentAtDesc(email);
    }

    /**
     * Saves a new message to the database.
     *
     * @param message the message entity to save
     * @return the saved message with generated ID
     */
    public Message saveMessage(Message message) {
        if (message.getReceiverEmail() == null || message.getReceiverEmail().isEmpty()) {
            message.setReceiverEmail("markdoan38@gmail.com");
        }
        if (message.getEmailRe() == null || message.getEmailRe().isEmpty()) {
            message.setEmailRe("markdoan38@gmail.com");
        }
        return messageRepository.save(message);
    }

    /**
     * Retrieves all messages in the system ordered by sent time descending.
     *
     * @return list of all messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAllByOrderBySentAtDesc();
    }

    /**
     * Counts the number of unread messages in the system.
     *
     * @return number of messages with status UNREAD
     */
    public long getUnreadCount() {
        return messageRepository.countByStatus(MessageStatus.UNREAD);
    }

    /**
     * Marks a specific message as read.
     *
     * @param id the unique identifier of the message
     */
    public void markAsRead(Long id) {
        Message message = messageRepository.findById(id).orElse(null);
        if (message != null) {
            message.setStatus(MessageStatus.READ);
            messageRepository.save(message);
        }
    }

    /**
     * Deletes a specific message from the database.
     *
     * @param id the unique identifier of the message to delete
     */
    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}