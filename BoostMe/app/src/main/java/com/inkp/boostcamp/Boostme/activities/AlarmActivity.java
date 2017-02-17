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

import com.inkp.boostcamp.Boostme.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by macbook on 2017. 2. 17..
 */

public class AlarmActivity extends AppCompatActivity{

    @BindView(R.id.button2)
    Button bt2;
    @OnClick(R.id.button2)
    public void clickedBt(View view){
        finish();
        return;
    }

    private Vibrator vibe;
    private SoundPool sp;
    private int soundID = 0;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_alarm);
        ButterKnife.bind(this);
        Sound();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sp.stop(soundID);

    }

    private void Sound() {
        Log.e("#### Sound", "OK");
        sp = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        soundID = sp.load(this, R.raw.bojangles, 20);

        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int soundID, int status) {
                soundPool.play(soundID, 1, 1, 0, -1, 1);
            }
        });
    }

}
