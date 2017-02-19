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
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
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
        small_title = intent.getStringExtra(Utills.ALARM_intent_small_title);

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<SmallScheduleRealm> mSmallSchedule
                = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", schedule_id).findAllSorted("order_value", Sort.ASCENDING);

        if(mSmallSchedule.size() == 0){
            //cancelAlarm(setPendingIntent(Utills.alarmIdBuilder(schedule_id, idx), intent), context);
            Log.d("mSmallSize", "Small schedule is empty, db error");
            return;
        }



        //반복성 schedule인 경우
        if (week_of_days != 0) {
            Log.d("Receiver", "repeat week day");
            int check_day = Utills.checkTargetWeekOfDayIsSet(week_of_days, calendar.get(Calendar.DAY_OF_WEEK));

            if (check_day != 0) {
                if(idx != mSmallSchedule.size() - 1) {
                    intent.putExtra(Utills.ALARM_intent_small_title, mSmallSchedule.get(idx+1).getSmall_tilte());
                    intent.putExtra(Utills.ALARM_intent_scheduleIdx, idx+1);

                    int new_alarm_id = Utills.alarmIdBuilder(schedule_id, idx+1);
                    long alert_time_long = mSmallSchedule.get(idx+1).getAlarm_start_time();
                    long triggertime = Utills.setTriggerTime(alert_time_long);

                    setPendingIntent(new_alarm_id, intent);
                    Utills.enrollAlarm(context, setPendingIntent(new_alarm_id, intent), triggertime);
                    }
                context.startActivity(makeAlarmActivityIntent());
            }
            else {
                Log.d("Receiver", "repeat but not today");
                Utills.enrollAlarm(context, setPendingIntent(Utills.alarmIdBuilder(schedule_id, idx), intent), System.currentTimeMillis() + 24 * 60 * 60 * 1000);
            }
        }
        else {
            Log.d("Receiver", "one shot");

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    //mSmallSchedule.get(idx).setAlarm_flag(false);
                    SmallScheduleRealm schedule = bgRealm.where(SmallScheduleRealm.class).equalTo("schedule_id", schedule_id).equalTo("order_value", idx).findFirst();
                    schedule.setAlarm_flag(false);
                    bgRealm.insertOrUpdate(schedule);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("REALM", "main Data Sucess");
                }
            });


            if(idx == mSmallSchedule.size()-1) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        ScheduleRealm schedule = bgRealm.where(ScheduleRealm.class).equalTo("id", schedule_id).findFirst();
                        schedule.setAlarm_flag(false);
                        bgRealm.insertOrUpdate(schedule);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("REALM", "main Data Sucess");
                    }
                });
                context.startActivity(makeAlarmActivityIntent());
                return;
            }

            idx++;
            while(true) {
                if (idx >= mSmallSchedule.size() - 1) {
                    if(mSmallSchedule.get(idx).isAlarm_flag() == false) {
                        idx++;
                        continue;
                    }
                    intent.putExtra(Utills.ALARM_intent_small_title, mSmallSchedule.get(idx).getSmall_tilte());
                    intent.putExtra(Utills.ALARM_intent_scheduleIdx, idx);

                    int new_alarm_id = Utills.alarmIdBuilder(schedule_id, idx);
                    long alert_time_long = mSmallSchedule.get(idx).getAlarm_start_time();
                    long triggertime = Utills.setTriggerTime(alert_time_long);

                    setPendingIntent(new_alarm_id, intent);
                    Utills.enrollAlarm(context, setPendingIntent(new_alarm_id, intent), triggertime);
                    break;
                }else break;
            }
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
