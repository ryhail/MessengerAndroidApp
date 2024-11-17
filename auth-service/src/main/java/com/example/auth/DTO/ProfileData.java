package com.example.auth.DTO;

import com.example.auth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Builder
public class ProfileData {
    private Long id;
    private String nickname;
    private String bio;
    private Date registrationDate;
    private String profilePicture;
    private Boolean privacyStatus;
}
