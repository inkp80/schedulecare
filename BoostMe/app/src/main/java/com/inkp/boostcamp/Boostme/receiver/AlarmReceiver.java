package com.inkp.boostcamp.Boostme.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.inkp.boostcamp.Boostme.Utills;

/**
 * Created by macbook on 2017. 2. 16..
 */

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        int Schedule_id = intent.getIntExtra(Utills.ALARM_intent_scheduleId, 0);
        int idx = intent.getIntExtra(Utills.ALARM_intent_scheduleIdx, 0);
        String title = intent.getStringExtra(Utills.ALARM_intent_title);
        long date_in_long = intent.getLongExtra(Utills.ALARM_intent_date, 0);
        int week_of_days = intent.getIntExtra(Utills.ALARM_intent_weekofday, 0);

        //String alarmId = String.valueOf(Schedule_id) + String.valueOf(idx);

        //PendingIntent pendingIntent
        //        = PendingIntent.getBroadcast(context, alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT );


        //if(idx == end...){

       // }

    }

    public static void cancelAlarm(PendingIntent pendingIntent, Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
