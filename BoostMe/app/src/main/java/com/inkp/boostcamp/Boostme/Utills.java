package com.inkp.boostcamp.Boostme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;
import com.inkp.boostcamp.Boostme.data.TagListRealm;
import com.inkp.boostcamp.Boostme.data.TagRealm;
import com.inkp.boostcamp.Boostme.receiver.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by macbook on 2017. 2. 11..
 */

public class Utills {
    public static String access_Schedule_id = "schedule_id";
    public static int weekdays_requestCode = 1313;
    public static int weekdays_resultCode = 3131;

    public static int GOOGLE_AUTOCOMPLETE_REQUESTCODE = 1717;
    public static int GOOGLE_AUTOCOMPLETE_RESULTCODE = 7171;

    public static String ALARM_intent_scheduleId = "alarm_scheduleId";
    public static String ALARM_intent_scheduleIdx = "alarm_scheduleIdx";
    public static String ALARM_intent_title = "alarm_title";
    public static String ALARM_intent_date = "alarm_date";
    public static String ALARM_intent_weekofday = "alarm_weekofday";
    public static String ALARM_intent_alert_time_long = "alarm_alerttime_long";
    public static String ALARM_intent_small_title = "alarm_small_title";

    public static String INTENT_ACTION_EDIT_SCHEDULE = "action_edit_schedule";

    public static String[] WEEKDAYS = {null, "sun", "mon", "tue", "wed", "thu", "fri", "sat"};


    public static SimpleDateFormat format_hhmm_a = new SimpleDateFormat("hh : mm a");

    public static SimpleDateFormat format_a_hhmm = new SimpleDateFormat("a hh : mm");

    public static SimpleDateFormat format_yymmdd_hhmm_a = new SimpleDateFormat("yy-MM-dd, a hh:mm");

    synchronized public static int getNextKeyMainSchedule(Realm realm) {
        if (realm.where(ScheduleRealm.class).max("id") == null) return 1;
        else
            return realm.where(ScheduleRealm.class).max("id").intValue() + 1;
    }

    synchronized public static int getNextKeySmallSchedule(Realm realm) {
        if (realm.where(SmallScheduleRealm.class).max("id") == null) return 1;
        else
            return realm.where(SmallScheduleRealm.class).max("id").intValue() + 1;
    }

    synchronized public static int getNextKeyTagList(Realm realm) {
        if (realm.where(TagListRealm.class).max("id") == null) return 1;
        else
            return realm.where(TagListRealm.class).max("id").intValue() + 1;
    }

    synchronized public static int getNextKeyTag(Realm realm) {
        if (realm.where(TagRealm.class).max("id") == null) return 1;
        else
            return realm.where(TagRealm.class).max("id").intValue() + 1;
    }


    public static int checkTargetWeekOfDayIsSet(int val, int Target_WeekOfDay) {
        int flag = val & (1 << Target_WeekOfDay);
        if (flag != 0)
            return (1 << Target_WeekOfDay);
        else return 0;
    }


    public static int alarmIdBuilder(int schedule_id, int index) {
        String Id_builder = String.valueOf(schedule_id) + String.valueOf(index);
        return Integer.valueOf(Id_builder);
    }

    public static void enrollAlarm(Context context, PendingIntent pendingIntent, long trigger_time) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        //Calendar calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= 23) {
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(trigger_time, pendingIntent);
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, trigger_time, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger_time, pendingIntent);
            }
        }
    }

    public static void cancleAlarm(Context context, int target_id, RealmResults<SmallScheduleRealm> obect_list) {
        for (int i = 0; i < obect_list.size(); i++) {
            int ALARM_ID = Utills.alarmIdBuilder(target_id, i);
            Intent intentForCancle = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntentForCancle
                    = PendingIntent.getBroadcast(context, ALARM_ID, intentForCancle, 0);
            AlarmReceiver.cancelAlarm(pendingIntentForCancle, context);
        }
    }


    public static void cancleAlarmInMain(Context context, int target_id, final RealmResults<SmallScheduleRealm> obect_list) {
        Realm realm = Realm.getDefaultInstance();
        for (int i = 0; i < obect_list.size(); i++) {

            final int id_small = obect_list.get(i).getId();
            final int idx = i;
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    SmallScheduleRealm small_schedule = bgRealm.where(SmallScheduleRealm.class).equalTo("id", id_small).findFirst();
                    small_schedule.setAlarm_flag(false);
                    bgRealm.insertOrUpdate(small_schedule);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("REALM", "small Data Sucess");
                }
            });

            int ALARM_ID = Utills.alarmIdBuilder(target_id, i);
            Intent intentForCancle = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntentForCancle
                    = PendingIntent.getBroadcast(context, ALARM_ID, intentForCancle, 0);
            AlarmReceiver.cancelAlarm(pendingIntentForCancle, context);
        }
    }


    public static long setTriggerTime(long input_time_long) {
        Calendar calendar = Calendar.getInstance();
        long cur_time_long = System.currentTimeMillis();
        if (cur_time_long < input_time_long) {
            return input_time_long;
        } else {
            long return_time = cur_time_long + (cur_time_long - input_time_long);
            return return_time;
        }
    }

    public static void alarmRegister(Context context, int schedule_id) {
        Realm realm = Realm.getDefaultInstance();
        Intent intent = new Intent(context, AlarmReceiver.class);
        ScheduleRealm main_schedule = realm.where(ScheduleRealm.class).equalTo("id", schedule_id).findFirst();
        RealmResults<SmallScheduleRealm> small_schedule = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", schedule_id).findAll();
        Log.d("####", String.valueOf(small_schedule.size()));
        long trigger_time = 0;
        int alarm_id = 0;

        Calendar calendar = Calendar.getInstance();
        //get start alarm schedule

        for (int i = 0; i < small_schedule.size(); i++) {
            if (small_schedule.get(i).isAlarm_flag()) {

                trigger_time = small_schedule.get(i).getAlarm_start_time();
                Date triger_date = new Date(trigger_time);
                calendar.setTime(triger_date);
                alarm_id = Utills.alarmIdBuilder(schedule_id, i);

                intent.putExtra(Utills.ALARM_intent_scheduleId, schedule_id); //메인아이디
                intent.putExtra(Utills.ALARM_intent_title, main_schedule.getTitle()); //메인 타이틀
                intent.putExtra(Utills.ALARM_intent_date, main_schedule.getDate_in_long()); //메인 시간
                intent.putExtra(Utills.ALARM_intent_scheduleIdx, i); //세부 인덱스
                intent.putExtra(Utills.ALARM_intent_weekofday, main_schedule.getWeek_of_day_repit()); //요일 반복
                intent.putExtra(Utills.ALARM_intent_small_title, small_schedule.get(i).getSmall_tilte());
                break;
            }
        }
        Log.d("triggertime", String.valueOf(trigger_time));

        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(context, alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Utills.enrollAlarm(context, pendingIntent, trigger_time);
    }

    public static void cancelAlarmByMainButton(Context context, int main_schedule_id) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SmallScheduleRealm> small_objs = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", main_schedule_id).findAll();
        Log.d("cancelAlarmButton", "start with size, " + small_objs.size());
        cancleAlarmInMain(context, main_schedule_id, small_objs);
    }

    public static void setAlarmByMainButton(Context context, int main_schedule_id, ScheduleRealm main_obj) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SmallScheduleRealm> small_objs = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", main_schedule_id).findAll();
        Log.d("setAlarmButton", "start with size, " + small_objs.size());


        Intent intent = new Intent(context, AlarmReceiver.class);
        long trigger_time = 0;
        int alarm_id = 0;
        Calendar calendar = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        //Log.d("alarm size", String.valueOf(smallSchedules.size()));

        boolean alarm_is_set = false;

        for (int i = 0; i < small_objs.size(); i++) {


            trigger_time = small_objs.get(i).getAlarm_start_time();


            //알람 필터링
            Date triger_date = new Date(trigger_time);
            calendar.setTime(triger_date);

            if ((current.getTimeInMillis() > trigger_time) && (main_obj.getWeek_of_day_repit() == 0)) {
                Log.d("시간", "시간이 맞지 않음");
                continue; //현재 시간보다 알림 시간이 이전 시간이면서, 동시에 요일 반복도 설정이 안되어 있으면 알람 등록하지 않음.
            }

            final int small_id = small_objs.get(i).getId();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    SmallScheduleRealm small_schedule = bgRealm.where(SmallScheduleRealm.class).equalTo("id", small_id).findFirst();
                    small_schedule.setAlarm_flag(true);
                    bgRealm.insertOrUpdate(small_schedule);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("REALM", "small Data Sucess");
                }
            });

            Log.d("bef to", "alarm");
            //메인 아이디, 메인 타이틀, 메인 시간, 세부 인덱스, 요일반복
            if (!alarm_is_set) {
                Log.d("in to", "alarm");
                alarm_is_set = true;
                alarm_id = Utills.alarmIdBuilder(main_schedule_id, 0);
                intent.putExtra(Utills.ALARM_intent_scheduleId, main_schedule_id); //메인아이디
                intent.putExtra(Utills.ALARM_intent_title, main_obj.getTitle()); //메인 타이틀
                intent.putExtra(Utills.ALARM_intent_date, main_obj.getDate_in_long()); //메인 시간
                intent.putExtra(Utills.ALARM_intent_scheduleIdx, i); //세부 인덱스
                intent.putExtra(Utills.ALARM_intent_weekofday, main_obj.getWeek_of_day_repit()); //요일 반복
                intent.putExtra(Utills.ALARM_intent_small_title, small_objs.get(i).getSmall_tilte());

                PendingIntent pendingIntent
                        = PendingIntent.getBroadcast(context, alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Utills.enrollAlarm(context, pendingIntent, Utills.setTriggerTime(trigger_time));
            }

        }

    }


}
