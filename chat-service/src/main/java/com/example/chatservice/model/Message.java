package com.example.chatservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

import java.util.Date;

import static org.hibernate.annotations.CascadeType.MERGE;
import static org.hibernate.annotations.CascadeType.PERSIST;


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
    private String text;
    private Date timestamp;
    @ManyToOne
    @JoinColumn(name = "chatter_id", referencedColumnName = "id")
    private Chatter sender;

    public Message(String text, Chatter sender) {
        this.text = text;
        this.sender = sender;
        this.timestamp = new Date();
    }
}
