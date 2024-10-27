package com.example.auth.service;


import com.example.auth.DTO.ProfileData;
import com.example.auth.model.Profile;
import com.example.auth.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;
    public List<Profile> findProfiles(String arg) {
        return null;
    }
    public ProfileData getProfileById(Long id) {
        if(repository.existsById(id)) {
            var profile =  repository.getReferenceById(id);
            ProfileData profileData = ProfileData.builder()
                    .id(profile.getId())
                    .nickname(profile.getNickname())
                    .bio(profile.getBio())
                    .profilePicture(profile.getProfilePicture())
                    .registrationDate(profile.getRegistrationDate())
                    .privacyStatus(profile.getPrivacy())
                    .build();
            return profileData;
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
