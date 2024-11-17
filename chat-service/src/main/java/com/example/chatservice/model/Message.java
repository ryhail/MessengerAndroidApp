package com.example.chatservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    private Date timestamp;
    private MessageType type;
    @ManyToOne
    @JoinColumn(name = "chatter_id", referencedColumnName = "id")
    private Chatter sender;

    public Message(String content, Chatter sender) {
        this.content = content;
        this.sender = sender;
        this.timestamp = new Date();
    }
}
