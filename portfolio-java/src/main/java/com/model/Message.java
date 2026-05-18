package com.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Message entity representing a contact message in the system.
 * Stores sender information, content, status, and receiver information.
 *
 * @author Portfolio Platform Team
 * @version 1.0
 */
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "receiver_email", nullable = false)
    private String receiverEmail;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    private MessageStatus status;

    @Column(name = "reply")
    private String reply;

    @Column(name = "replied_at")
    private LocalDateTime repliedAt;

    @Column(name = "email_re", nullable = false)
    private String emailRe = "markdoan38@gmail.com";

    /**
     * Default constructor
     */
    public Message() {
        this.emailRe = "markdoan38@gmail.com";
    }

    /**
     * Constructor with required fields
     *
     * @param name sender name
     * @param email sender email
     * @param subject message subject
     * @param content message content
     */
    public Message(String name, String email, String subject, String content) {
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.content = content;
        this.status = MessageStatus.UNREAD;
        this.sentAt = LocalDateTime.now();
        this.emailRe = "markdoan38@gmail.com";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public LocalDateTime getRepliedAt() {
        return repliedAt;
    }

    public void setRepliedAt(LocalDateTime repliedAt) {
        this.repliedAt = repliedAt;
    }

    public String getEmailRe() {
        return emailRe;
    }

    public void setEmailRe(String emailRe) {
        this.emailRe = emailRe;
    }
}