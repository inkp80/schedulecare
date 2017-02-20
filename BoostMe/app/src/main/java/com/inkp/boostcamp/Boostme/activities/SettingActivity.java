package com.inkp.boostcamp.Boostme.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.inkp.boostcamp.Boostme.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macbook on 2017. 2. 16..
 */

public class SettingActivity extends AppCompatActivity{

    @BindView(R.id.setting_tag)
    TextView mTagTextView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setSupportActionBar((Toolbar) findViewById(R.id.setuo_toolbar));
        ButterKnife.bind(this);

        mTagTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ShowTagActivity.class);
                startActivity(intent);
            }
        });
    }

}
