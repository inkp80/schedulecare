package com.inkp.boostcamp.Boostme.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by macbook on 2017. 2. 17..
 */

public class AlarmActivity extends AppCompatActivity{

    Realm realm;

    @BindView(R.id.alarm_title)
    TextView mAlarmTitle;

    TextView mAlarmSmall;
    @BindView(R.id.alarm_time)
    TextView mAlarmTime;

    @BindView(R.id.alarm_next_schedule_title)
    TextView mAlarmNextTitle;
    @BindView(R.id.alarm_next_schedule_time)
    TextView mAlarmNextTime;


    @BindView(R.id.button2)
    Button bt2;
    @OnClick(R.id.button2)
    public void clickedBt(View view){
        //vibe.cancel();
        //sp.stop(SOUND_ID);
        finish();
        return;
    }

    private Vibrator vibe;
    private SoundPool sp;
    private int SOUND_ID = 0;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        realm = Realm.getDefaultInstance();
        RealmResults<SmallScheduleRealm> results = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", intent.getIntExtra(Utills.ALARM_intent_scheduleId, 0)).findAll();

        Date tmp = new Date();
        tmp.setTime(intent.getLongExtra(Utills.ALARM_intent_date, 0));
        mAlarmTitle.setText(intent.getStringExtra(Utills.ALARM_intent_title) + " - " +  intent.getStringExtra(Utills.ALARM_intent_small_title));
        mAlarmTime.setText(Utills.format_a_hhmm.format(tmp));

        int next_idx = intent.getIntExtra(Utills.ALARM_intent_small_next_idx, -1);
        Log.d("idx data", String.valueOf(next_idx));
        Log.d("result.size", String .valueOf(results.size()));

        if(next_idx != -1) {
            Calendar nextTime = Calendar.getInstance();
            nextTime.setTimeInMillis(results.get(next_idx).getAlarm_start_time());

            mAlarmNextTitle.setText(results.get(next_idx).getSmall_tilte().toString());
            mAlarmNextTime.setText(Utills.format_a_hhmm.format(new Date(nextTime.getTimeInMillis())));
        } else{
            mAlarmNextTitle.setText("없음");
        }

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        } //sp = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        SOUND_ID = sp.load(this, R.raw.bojangles, 20);
        checkMode();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        vibe.cancel();
        sp.stop(SOUND_ID);
    }

    @Override
    protected void onPause(){
        super.onPause();
        vibe.cancel();
        sp.stop(SOUND_ID);
    }

    private void makeSound() {

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundID, int status) {
                soundPool.play(soundID, 1, 1, 0, -1, 1);
            }
        });
    }

    private void makeVibrate() {
        long[] patten = {0, 1000, 500, 2000, 1000};
        vibe.vibrate(patten, 0);
    }

    private void checkMode(){
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                makeVibrate();
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                makeVibrate();
                makeSound();
                break;
        }
    }

}
