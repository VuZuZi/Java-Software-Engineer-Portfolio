package com.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversations")
@Setter
@Getter
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    private String sender;

    private String senderEmail;

    @Column(name = "last_message")
    private String lastMessage;

    @Column(name = "last_message_time")
    private LocalDateTime lastMessageTime;

    @Column(name = "unread_count")
    private int unreadCount = 0;

//    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Message> messages = new ArrayList<>();
    @Transient
    private List<Message> messages = new ArrayList<>();
}
