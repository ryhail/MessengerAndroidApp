package com.example.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import javax.swing.text.StyledEditorKit;
import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profiles")
public class Profile {
    @Id
    @Column(unique = true, nullable = false)
    private Long id;

    @MapsId("id")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    private String nickname;
    private String bio;
    @Column(name = "registration_date")
    private Date registrationDate;
    @Column(name = "profile_picture")
    private String profilePicture;
    private Boolean privacy;
}
