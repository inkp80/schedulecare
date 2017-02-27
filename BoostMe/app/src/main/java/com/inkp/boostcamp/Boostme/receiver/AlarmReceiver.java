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

public class AlarmReceiver extends BroadcastReceiver {
    Context mContext;

    //int next_idx=-1;
    int schedule_id;
    int idx;
    String title;
    long date_in_long;
    int week_of_days;
    String small_title;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent;

        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date); //오늘 날짜 설정


        schedule_id = intent.getIntExtra(Utills.ALARM_intent_scheduleId, 0);

        idx = intent.getIntExtra(Utills.ALARM_intent_scheduleIdx, 0);
        title = intent.getStringExtra(Utills.ALARM_intent_title);
        date_in_long = intent.getLongExtra(Utills.ALARM_intent_date, 0);
        week_of_days = intent.getIntExtra(Utills.ALARM_intent_weekofday, 0);
        small_title = intent.getStringExtra(Utills.ALARM_intent_small_title);

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<SmallScheduleRealm> mSmallSchedule
                = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", schedule_id).findAllSorted("order_value", Sort.ASCENDING);

        if (mSmallSchedule.size() == 0) {
            //cancelAlarm(setPendingIntent(Utills.alarmIdBuilder(schedule_id, idx), intent), context);
            Log.d("mSmallSize", "Small schedule is empty, db error");
            return;
        }


        //반복성 schedule인 경우
        if (week_of_days != 0) {
            Log.d("Receiver", "repeat week day");
            int check_day = Utills.checkTargetWeekOfDayIsSet(week_of_days, calendar.get(Calendar.DAY_OF_WEEK));

            if (check_day != 0) {
                //해당 요일이 맞다
                final int sem = idx;
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm bgRealm) {
                        //mSmallSchedule.get(idx).setAlarm_flag(false);
                        SmallScheduleRealm schedule = bgRealm.where(SmallScheduleRealm.class).equalTo("schedule_id", schedule_id).equalTo("order_value", sem).findFirst();
                        schedule.setAlarm_flag(false);
                        bgRealm.insertOrUpdate(schedule);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.d("REALM", "main Data Sucess");
                    }
                });


                while (true) {
                    idx++;
                    if (idx == mSmallSchedule.size()) break;
                    //포문으로 알람 설정된 것 찾아서 등록
                    if (mSmallSchedule.get(idx).isAlarm_flag()) {
                        intent.putExtra(Utills.ALARM_intent_small_title, mSmallSchedule.get(idx).getSmall_tilte());
                        intent.putExtra(Utills.ALARM_intent_scheduleIdx, idx);

                        int new_alarm_id = Utills.alarmIdBuilder(schedule_id, idx);
                        long alert_time_long = mSmallSchedule.get(idx).getAlarm_start_time();
                        long triggertime = Utills.setTriggerTime(alert_time_long);

                        setPendingIntent(new_alarm_id, intent);
                        Utills.enrollAlarm(context, setPendingIntent(new_alarm_id, intent), triggertime);
                        //next_idx = idx;
                        break;
                    }
                }
                context.startActivity(makeAlarmActivityIntent());
            } else {
                Log.d("Receiver", "repeat, not today");
                Utills.enrollAlarm(context, setPendingIntent(Utills.alarmIdBuilder(schedule_id, idx), intent), System.currentTimeMillis() + 24 * 60 * 60 * 1000);
            }
        } else {
            Log.d("Receiver", "one shot");

            Log.d("####", String.valueOf(idx));
            final int sem = idx;
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    //mSmallSchedule.get(idx).setAlarm_flag(false);
                    RealmResults<SmallScheduleRealm> mScheduleObj = bgRealm.where(SmallScheduleRealm.class).equalTo("schedule_id", schedule_id).findAllSorted("order_value",Sort.ASCENDING);
                    mScheduleObj.get(sem).setAlarm_flag(false);
                    bgRealm.insertOrUpdate(mScheduleObj);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("REALM", "main Data Sucess");
                }
            });




            while (true) {
                idx++;
                if (idx == mSmallSchedule.size()) break;

                intent.putExtra(Utills.ALARM_intent_scheduleId, schedule_id);
                intent.putExtra(Utills.ALARM_intent_small_title, mSmallSchedule.get(idx).getSmall_tilte());
                intent.putExtra(Utills.ALARM_intent_scheduleIdx, idx);

                int new_alarm_id = Utills.alarmIdBuilder(schedule_id, idx);
                long alert_time_long = mSmallSchedule.get(idx).getAlarm_start_time();
                long triggertime = Utills.setTriggerTime(alert_time_long);

                setPendingIntent(new_alarm_id, intent);
                Utills.enrollAlarm(context, setPendingIntent(new_alarm_id, intent), triggertime);
                //next_idx = idx;
                break;
            }
            context.startActivity(makeAlarmActivityIntent());
        }

    }


    public static void cancelAlarm(PendingIntent pendingIntent, Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public PendingIntent setPendingIntent(int alarm_id, Intent intent) {
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(mContext, alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    public Intent makeAlarmActivityIntent() {
        Intent intent = new Intent(mContext, AlarmActivity.class);
        intent.putExtra(Utills.ALARM_intent_title, title);
        intent.putExtra(Utills.ALARM_intent_date, date_in_long);
        intent.putExtra(Utills.ALARM_intent_small_title, small_title);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra(Utills.ALARM_intent_small_next_idx, next_idx);
        intent.putExtra(Utills.ALARM_intent_scheduleId, schedule_id);
        return intent;
    }
}
