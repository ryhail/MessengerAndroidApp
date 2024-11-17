package com.example.messanger.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Profile {
    private Long id;
    private String nickname;
    private String bio;
    private Date registrationDate;
    private String profilePicture;
    private Boolean privacyStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRegistrationDateString() {
        return SimpleDateFormat.getDateInstance().format(registrationDate);
    }
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Boolean getPrivacyStatus() {
        return privacyStatus;
    }

    public void setPrivacyStatus(Boolean privacyStatus) {
        this.privacyStatus = privacyStatus;
    }
}
