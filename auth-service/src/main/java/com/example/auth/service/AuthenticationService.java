package com.example.auth.service;

import com.example.auth.DTO.JwtAuthenticationResponse;
import com.example.auth.DTO.SignInRequest;
import com.example.auth.DTO.SignUpRequest;
import com.example.auth.model.Profile;
import com.example.auth.model.Role;
import com.example.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .device(request.getDevice())
                .build();
        user = userService.create(user);
        user.setUserProfile(Profile.builder()
                .id(user.getId())
                .nickname(request.getUsername())
                .bio("Новый пользователь")
                .registrationDate(new Date())
                .profilePicture("")
                .privacy(false)
                .build());
        userService.save(user);
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());
        User user_data = userService.getByUsername(request.getUsername());
        if(!Objects.equals(user_data.getDevice(), request.getDevice())) {
            user_data.setDevice(request.getDevice());
            userService.save(user_data);
        }
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}