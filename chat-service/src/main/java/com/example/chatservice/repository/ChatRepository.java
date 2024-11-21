package com.example.chatservice.repository;

import com.example.chatservice.model.Chat;
import com.example.chatservice.model.Chatter;
import com.example.chatservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> getChatsByChattersId(Long id);
    Boolean existsByChattersEquals(Set<Chatter> chatters);
    @Query("SELECT c FROM Chat c " +
            "WHERE :size = SIZE(c.chatters) AND " +
            "NOT EXISTS (" +
            "    SELECT C FROM c.chatters C WHERE C NOT IN :chatters)")
    Optional<Chat> getChatByChatters(@Param("chatters") Set<Chatter> chatters, @Param("size") long size);
    @Query("SELECT m FROM Chat c JOIN c.messages m WHERE c.id = :chat_id AND m.timestamp > :timestamp")
    List<Message> getMessagesAfterTimestamp(@Param("chat_id") Long chatId, @Param("timestamp") Date timestamp);
}
