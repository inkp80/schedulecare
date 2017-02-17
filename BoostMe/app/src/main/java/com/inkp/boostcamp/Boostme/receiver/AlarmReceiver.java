package com.inkp.boostcamp.Boostme.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.activities.AlarmActivity;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.Util;

/**
 * Created by macbook on 2017. 2. 16..
 */

public class AlarmReceiver extends BroadcastReceiver{
    Context mContext;

    int schedule_id;
    int idx;
    String title;
    long date_in_long;
    int week_of_days;

    String small_title;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("alarm Received", "alarm");
        Toast.makeText(context, "Alaamr", Toast.LENGTH_SHORT).show();

        mContext = context;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent;

        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date); //오늘 날짜 설정


        schedule_id = intent.getIntExtra(Utills.ALARM_intent_scheduleId, 0);
        Log.d("schedule id", String.valueOf(schedule_id));
        idx = intent.getIntExtra(Utills.ALARM_intent_scheduleIdx, 0);
        title = intent.getStringExtra(Utills.ALARM_intent_title);
        date_in_long = intent.getLongExtra(Utills.ALARM_intent_date, 0);
        week_of_days = intent.getIntExtra(Utills.ALARM_intent_weekofday, 0);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<SmallScheduleRealm> mSmallSchedule
                = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", schedule_id).findAll();
        Log.d("mSmallSize", String.valueOf(mSmallSchedule.size()));
        if(mSmallSchedule.size() == 0){
            //cancelAlarm(setPendingIntent(Utills.alarmIdBuilder(schedule_id, idx), intent), context);
            return;
        }

        small_title = mSmallSchedule.get(idx).getSmall_tilte();



        long alert_time_long = intent.getLongExtra(Utills.ALARM_intent_alert_time_long, 0);
        long triggertime = Utills.setTriggerTime(alert_time_long);


        //반복성 schedule인 경우
        if (week_of_days != 0) {
            int check_day = Utills.checkTargetWeekOfDayIsSet
                    (week_of_days, calendar.get(Calendar.DAY_OF_WEEK));
            if (check_day != 0) {
                Intent alert_intent = new Intent(context, AlarmActivity.class);
                alert_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(alert_intent);
            } else {
                //Utills.enrollAlarm(context, pendingIntent);
                //재등록 24시간 이후로
            }
        }
        else{ //단발성 알람
            context.startActivity(makeAlarmActivityIntent());

        }
    }

    public static void cancelAlarm(PendingIntent pendingIntent, Context context) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public PendingIntent setPendingIntent(int alarm_id, Intent intent){
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(mContext, alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public Intent makeAlarmActivityIntent(){
        Intent intent = new Intent(mContext, AlarmActivity.class);
        intent.putExtra(Utills.ALARM_intent_title, title);
        intent.putExtra(Utills.ALARM_intent_date, date_in_long);
        intent.putExtra(Utills.ALARM_intent_small_title, small_title);
        return intent;
    }
}
