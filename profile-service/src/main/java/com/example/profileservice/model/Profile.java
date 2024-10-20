package com.example.profileservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Profile {
    @Id
    private Long id;
    private String name;
    private String bio;
    private String picture;
    private Boolean privacy;
}
