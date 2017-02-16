package com.inkp.boostcamp.Boostme.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by macbook on 2017. 2. 16..
 */

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //
        //long Schedule_id = intent.get;
        int idx;
        int week_of_days;

        //String alarmId = String.valueOf(Schedule_id) + String.valueOf(idx);
        //

        //PendingIntent pendingIntent
        //        = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT );

    }

    public static void cancelAlarm(PendingIntent pendingIntent, Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
