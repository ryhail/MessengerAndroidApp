package com.example.auth.service;
import com.example.auth.DTO.MessageData;
import com.example.auth.DTO.NotificationRequest;
import com.example.auth.DTO.ProfileData;
import com.example.auth.model.Profile;
import com.example.auth.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final UserService userService;
    private final ProfileService profileService;
    public void sendNotification(NotificationRequest request) {
        try {
            User recipient = userService.get(request.getRecipient_id());
            ProfileData sender = profileService.getProfileById(request.getMsg().getSenderId());
            String token = recipient.getDevice();
            if(token == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            if(request.getMsg() == null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson = objectMapper.writeValueAsString(request.getMsg());
            Message message = Message.builder()
                    .putData("user_name", recipient.getUsername())
                    .putData("user_id", recipient.getId().toString())
                    .putData("chat_id", request.getChat_id().toString())
                    .putData("chat_name", request.getChatName())
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(request.getChatName())
                            .setBody(sender.getNickname() + ": " + request.getMsg().getContent())
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            response.length();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
