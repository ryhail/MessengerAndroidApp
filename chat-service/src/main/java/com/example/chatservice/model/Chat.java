package com.example.chatservice.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.springframework.data.repository.cdi.Eager;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.hibernate.annotations.CascadeType.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    @Cascade({PERSIST, MERGE})
    private List<Message> messages = new LinkedList<Message>();
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chat_chatter",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "chatter_id"))
    @Cascade({PERSIST, MERGE})
    private Set<Chatter> chatters = new HashSet<Chatter>();

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public String getNameForUser(Long id) {
        for (Chatter chatter : chatters) {
            if (!chatter.getId().equals(id)) {
                return chatter.getName();
            }
        }
        return chatters.stream().findFirst().get().getName();
    }
}
