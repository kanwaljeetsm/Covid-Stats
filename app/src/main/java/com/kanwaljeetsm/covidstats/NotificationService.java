package com.kanwaljeetsm.covidstats;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotifications(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

    }
    public void showNotifications(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "GeneralNotifications")
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);


        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(777, builder.build());


    }
}
