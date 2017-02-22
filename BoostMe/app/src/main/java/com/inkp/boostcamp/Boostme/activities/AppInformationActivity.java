package com.inkp.boostcamp.Boostme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.inkp.boostcamp.Boostme.R;

/**
 * Created by inkp on 2017-02-23.
 */

public class AppInformationActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        setSupportActionBar((Toolbar) findViewById(R.id.verinform_toolbar));
    }
}
