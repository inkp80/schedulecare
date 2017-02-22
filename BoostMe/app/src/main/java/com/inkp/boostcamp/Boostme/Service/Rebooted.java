package com.inkp.boostcamp.Boostme.Service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;
import com.inkp.boostcamp.Boostme.receiver.AlarmReceiver;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by inkp on 2017-02-22.
 */

public class Rebooted extends IntentService {

    Context mContext;
    Realm realm;
    RealmResults<ScheduleRealm> mSchedule;
    RealmResults<SmallScheduleRealm> mScheduleList;

    Calendar calendar =Calendar.getInstance();

    public Rebooted(){
        super("Rebooted");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("reboot", "reboot, start");
        realm = Realm.getDefaultInstance();
        mSchedule = realm.where(ScheduleRealm.class).findAll();
        for(int i=0; i<mSchedule.size(); i++){
            mScheduleList = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", mSchedule.get(i).getId()).findAll();
            RegistAlarm(mContext, mScheduleList, mSchedule.get(i));
        }

        return;
    }

    public void RegistAlarm(Context context, final RealmResults<SmallScheduleRealm> small_schedule_lists, ScheduleRealm main_schedule){
        for(int i=0; i<small_schedule_lists.size(); i++){
            if(small_schedule_lists.get(i).isAlarm_flag()){
                Calendar trigger_time = Calendar.getInstance();
                trigger_time.setTimeInMillis(small_schedule_lists.get(i).getAlarm_start_time());
                if(calendar.getTimeInMillis() > trigger_time.getTimeInMillis()) {
                    final int idx = i;
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            SmallScheduleRealm schedule = bgRealm.where(SmallScheduleRealm.class).equalTo("id", small_schedule_lists.get(idx).getId()).findFirst();
                            schedule.setAlarm_flag(false);
                            bgRealm.insertOrUpdate(schedule);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Log.d("REALM", "small main Data Sucess");
                        }
                    });
                    continue;
                }
                Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
                intent.putExtra(Utills.ALARM_intent_scheduleId, main_schedule.getId()); //메인아이디
                intent.putExtra(Utills.ALARM_intent_title, main_schedule.getTitle()); //메인 타이틀
                intent.putExtra(Utills.ALARM_intent_date, main_schedule.getDate_in_long()); //메인 시간
                intent.putExtra(Utills.ALARM_intent_scheduleIdx, i); //세부 인덱스
                intent.putExtra(Utills.ALARM_intent_weekofday, main_schedule.getWeek_of_day_repit()); //요일 반복
                intent.putExtra(Utills.ALARM_intent_small_title, small_schedule_lists.get(i).getSmall_tilte());

                int alarm_id = Utills.alarmIdBuilder(main_schedule.getId(), i);
                PendingIntent pendingIntent
                        = PendingIntent.getBroadcast(getBaseContext(), alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Utills.enrollAlarm(getBaseContext(), pendingIntent, Utills.setTriggerTime(trigger_time.getTimeInMillis()));
                return;
            }
        }

    }
}
