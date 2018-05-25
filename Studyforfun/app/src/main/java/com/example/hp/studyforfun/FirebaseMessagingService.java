package com.example.hp.studyforfun;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by hp on 31-12-2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String message = remoteMessage.getData().get("message");
        String title = remoteMessage.getData().get("title");
        String email = remoteMessage.getData().get("email");
        String name = remoteMessage.getData().get("name");
        String profile =  remoteMessage.getData().get("profile");

        NotificationCompat.Builder notify = new NotificationCompat.Builder(this);
        notify.setContentTitle(title);
        notify.setContentText(message);
        notify.setTicker("You have new Messages");
        notify.setSmallIcon(R.drawable.book5);
        notify.setAutoCancel(true);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        long[] pattern = {500,500};
        notify.setSound(sound);
        notify.setVibrate(pattern);


        Intent i = new Intent(this,chat.class);
        i.putExtra("email",email);
        i.putExtra("name",name);
        i.putExtra("profile",profile);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_ONE_SHOT);
        notify.setContentIntent(pi);

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0,notify.build());

    }


}
