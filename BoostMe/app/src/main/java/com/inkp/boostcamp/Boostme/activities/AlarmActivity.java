package com.inkp.boostcamp.Boostme.activities;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
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

import org.w3c.dom.Text;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by macbook on 2017. 2. 17..
 */

public class AlarmActivity extends AppCompatActivity{

    @BindView(R.id.alarm_title)
    TextView mAlarmTitle;
    @BindView(R.id.alarm_small_title)
    TextView mAlarmSmall;
    @BindView(R.id.alarm_time)
    TextView mAlarmTime;


    @BindView(R.id.button2)
    Button bt2;
    @OnClick(R.id.button2)
    public void clickedBt(View view){
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
        Date tmp = new Date();
        tmp.setTime(intent.getLongExtra(Utills.ALARM_intent_date, 0));
        mAlarmTitle.setText(intent.getStringExtra(Utills.ALARM_intent_title));
        mAlarmSmall.setText(intent.getStringExtra(Utills.ALARM_intent_small_title));
        mAlarmTime.setText(Utills.format_yymmdd_hhmm_a.format(tmp));

        checkMode();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause(){
        super.onPause();
        vibe.cancel();
        sp.stop(SOUND_ID);
    }

    private void makeSound() {
        sp = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        SOUND_ID = sp.load(this, R.raw.bojangles, 20);

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundID, int status) {
                soundPool.play(soundID, 1, 1, 0, -1, 1);
            }
        });
    }

    private void makeVibrate() {
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
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
