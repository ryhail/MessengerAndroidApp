package com.example.auth.service;


import com.example.auth.DTO.ProfileData;
import com.example.auth.model.Profile;
import com.example.auth.model.User;
import com.example.auth.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;
    private final UserService userService;
    public List<ProfileData> findProfiles(String arg) {
        List<Profile> profiles = repository.findProfilesByNicknameStartingWithIgnoreCase(arg);
        if(profiles == null || profiles.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        else {
            List<ProfileData> profilesInfo = profiles.stream().filter(profile -> !profile.getPrivacy()).map(profile -> new ProfileData(
                            profile.getId(),
                            profile.getNickname(),
                            profile.getBio(),
                            profile.getRegistrationDate(),
                            profile.getProfilePicture(),
                            profile.getPrivacy()))
                    .toList();
            return profilesInfo;
        }
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
    public ProfileData updateProfilePicture(String uri) {
        Profile profile = repository.getReferenceById(
                userService.getCurrentUserData().getId());
        profile.setProfilePicture(uri);
        profile = repository.save(profile);
        return ProfileData.builder()
                .id(profile.getId())
                .nickname(profile.getNickname())
                .bio(profile.getBio())
                .profilePicture(profile.getProfilePicture())
                .registrationDate(profile.getRegistrationDate())
                .privacyStatus(profile.getPrivacy())
                .build();
    }

    public ProfileData updateProfile(ProfileData request) {
        if(request == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Profile profile = repository.getReferenceById(request.getId());
        profile.setNickname(request.getNickname());
        profile.setBio(request.getBio());
        profile.setPrivacy(request.getPrivacyStatus());
        profile = repository.save(profile);
        if(profile != null)
            return ProfileData.builder()
                    .id(profile.getId())
                    .nickname(profile.getNickname())
                    .bio(profile.getBio())
                    .profilePicture(profile.getProfilePicture())
                    .registrationDate(profile.getRegistrationDate())
                    .privacyStatus(profile.getPrivacy())
                    .build();
        else
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
