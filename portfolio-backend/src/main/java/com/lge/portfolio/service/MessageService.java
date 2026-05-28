package com.lge.portfolio.service;

import com.lge.portfolio.dto.MessageRequest;
import com.lge.portfolio.dto.MessageResponse;
import com.lge.portfolio.entity.ContactMessage;
import com.lge.portfolio.exception.ResourceNotFoundException;
import com.lge.portfolio.repository.ContactMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final ContactMessageRepository messageRepository;

    public List<MessageResponse> getAll() {
        return messageRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(MessageResponse::from)
                .toList();
    }

    @Transactional
    public MessageResponse create(MessageRequest request) {
        ContactMessage message = new ContactMessage();
        message.setSenderName(request.senderName().trim());
        message.setSenderEmail(request.senderEmail().trim().toLowerCase());
        message.setSubject(request.subject().trim());
        message.setContent(request.content().trim());
        message.setRead(false);

        return MessageResponse.from(messageRepository.save(message));
    }

    @Transactional
    public MessageResponse markAsRead(Long id) {
        ContactMessage message = findMessage(id);
        message.setRead(true);
        return MessageResponse.from(message);
    }

    @Transactional
    public void delete(Long id) {
        messageRepository.delete(findMessage(id));
    }

    private ContactMessage findMessage(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
    }
}
