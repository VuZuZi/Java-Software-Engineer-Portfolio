package com.lge.portfolio.dto;

import com.lge.portfolio.entity.ContactMessage;

import java.time.Instant;

public record MessageResponse(
        Long id,
        String senderName,
        String senderEmail,
        String subject,
        String content,
        boolean read,
        Instant createdAt
) {
    public static MessageResponse from(ContactMessage message) {
        return new MessageResponse(
                message.getId(),
                message.getSenderName(),
                message.getSenderEmail(),
                message.getSubject(),
                message.getContent(),
                message.isRead(),
                message.getCreatedAt()
        );
    }
}
