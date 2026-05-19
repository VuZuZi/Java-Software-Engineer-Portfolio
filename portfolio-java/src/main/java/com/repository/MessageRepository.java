package com.repository;

import com.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderEmailOrReceiverEmail(String senderEmail, String receiverEmail);

    List<Message> findByConversationIdOrderBySentAtAsc(String conversationId);

    @Query("SELECT DISTINCT m.conversationId FROM Message m WHERE m.conversationId IS NOT NULL ORDER BY m.sentAt DESC")
    List<String> findDistinctConversationIds();

    List<Message> findByReceiverEmailAndIsReadFalse(String receiverEmail);

    List<Message> findByConversationIdAndReceiverEmail(String conversationId, String receiverEmail);
}