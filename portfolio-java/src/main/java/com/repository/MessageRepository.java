package com.repository;

import com.model.Message;
import com.model.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository interface for Message entity operations.
 * Provides database access methods for message management.
 *
 * @author Portfolio Platform Team
 * @version 1.0
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    /**
     * Finds all messages exchanged between two users.
     * Returns messages where (sender = user1 and receiver = user2)
     * or (sender = user2 and receiver = user1).
     *
     * @param email1 the email address of the first user
     * @param email2 the email address of the second user
     * @return list of messages sorted by sent time ascending
     */
    @Query("SELECT m FROM Message m WHERE " +
            "(m.email = :email1 AND m.receiverEmail = :email2) OR " +
            "(m.email = :email2 AND m.receiverEmail = :email1) " +
            "ORDER BY m.sentAt ASC")
    List<Message> findConversationBetweenUsers(@Param("email1") String email1,
                                               @Param("email2") String email2);

    /**
     * Finds all messages sent by a specific user, ordered by sent time descending.
     *
     * @param email the email address of the sender
     * @return list of messages sent by the user
     */
    List<Message> findByEmailOrderBySentAtDesc(String email);

    /**
     * Finds all messages received by a specific user, ordered by sent time descending.
     *
     * @param receiverEmail the email address of the receiver
     * @return list of messages received by the user
     */
    List<Message> findByReceiverEmailOrderBySentAtDesc(String receiverEmail);

    /**
     * Finds all messages ordered by sent time descending.
     *
     * @return list of all messages
     */
    List<Message> findAllByOrderBySentAtDesc();

    /**
     * Counts messages with a specific status.
     *
     * @param status the message status to count
     * @return number of messages with the given status
     */
    long countByStatus(MessageStatus status);
}