package com.example.messanger.util;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.messanger.MessagesActivity;
import com.example.messanger.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Long userId;
    private Long chatId;
    private String userName;
    private String chatName;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            userName = remoteMessage.getData().get("user_name");
            chatName = remoteMessage.getData().get("chat_name");
            chatId = Long.parseLong(remoteMessage.getData().get("chat_id"));
            userId = Long.parseLong(remoteMessage.getData().get("user_id"));
            showNotification(title, message);
        }
    }

    private void showNotification(String title, String message) {
        String channelId = "message_channel";

        Intent intent = new Intent(this, MessagesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("CHAT_ID", chatId);
        intent.putExtra("USER_ID", userId);
        intent.putExtra("CHAT_NAME", chatName);
        intent.putExtra("USER_NAME", userName);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_chats)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelId, "Новое сообщение",
                NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(0, builder.build());
    }
}
