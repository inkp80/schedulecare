package com.inkp.boostcamp.Boostme;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.inkp.boostcamp.Boostme.activities.MainActivity;

/**
 * Created by inkp on 2017-02-22.
 */

public class NotificationUtills {

    public static void NotificationSomethings(Context context, Intent intent) {

        Resources res = context.getResources();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setAction("NOTI");


        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("open")
                .setContentText("this")
                .setTicker("title")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
        //Notification.D
        //action 추가하여 추가 알람 및 확인 만들기

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1234, builder.build());
    }

}
