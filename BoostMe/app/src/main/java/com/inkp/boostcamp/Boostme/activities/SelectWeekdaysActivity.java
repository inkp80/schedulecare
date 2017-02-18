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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.Utills;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.internal.Util;

/**
 * Created by macbook on 2017. 2. 13..
 */

public class SelectWeekdaysActivity extends AppCompatActivity implements View.OnClickListener {
    public int weekday_bit = 0;

    static final int[] checkboxs = {
            R.id.check_sun,
            R.id.check_mon,
            R.id.check_tue,
            R.id.check_wed,
            R.id.check_thu,
            R.id.check_fri,
            R.id.check_sat
    };

    static final int[] items = {
            R.id.week_of_day_sun,
            R.id.week_of_day_mon,
            R.id.week_of_day_tue,
            R.id.week_of_day_wed,
            R.id.week_of_day_thu,
            R.id.week_of_day_fri,
            R.id.week_of_day_sat
    };

    @BindView(R.id.toolbar_weekday_save)
    ImageButton mSaveWeekday;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_weekofdays);
        ButterKnife.bind(this);


        setSupportActionBar((Toolbar) findViewById(R.id.weekday_toolbar));
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));
        weekday_bit = getIntent().getIntExtra("curVal", 0);
        for (int item : items) {
            RelativeLayout item_layout = (RelativeLayout) findViewById(item);
            item_layout.setOnClickListener(this);
        }
        setCheckState();
        Toast.makeText(this, "요일 반복 설정 시, 날짜 설정 값은 무시됩니다.", Toast.LENGTH_SHORT).show();

        mSaveWeekday.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getDataFromBox();
                        Intent intentReturn = new Intent(SelectWeekdaysActivity.this, AddTaskActivity.class);
                        intentReturn.putExtra("weekdaysVal", weekday_bit);
                        setResult(Utills.weekdays_resultCode, intentReturn);
                        //Toast.makeText(SelectWeekdaysActivity.this, String.valueOf(weekday_bit), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar_weekofday, null);

        actionBar.setCustomView(actionbar);
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        View view = getSupportActionBar().getCustomView();
        ImageButton saveButton = (ImageButton) view.findViewById(R.id.weekofday_save_button);
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getDataFromBox();
                        Intent intentReturn = new Intent(SelectWeekdaysActivity.this, AddTaskActivity.class);
                        intentReturn.putExtra("weekdaysVal", weekday_bit);
                        setResult(Utills.weekdays_resultCode, intentReturn);
                        //Toast.makeText(SelectWeekdaysActivity.this, String.valueOf(weekday_bit), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
        //final ImageView saveListButton = (ImageView) view.findViewById(R.id.add_save_button);
        return true;
    }*/

    @Override
    public void onClick(View v) {
        CheckBox checkBox;
        int unCheck = (1 << 8) - 1; // 1111 1111
        switch (v.getId()) {
            case R.id.week_of_day_sun:
                checkBox = (CheckBox) findViewById(checkboxs[0]);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                break;

            case R.id.week_of_day_mon:
                checkBox = (CheckBox) findViewById(checkboxs[1]);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                break;

            case R.id.week_of_day_tue:
                checkBox = (CheckBox) findViewById(checkboxs[2]);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                break;

            case R.id.week_of_day_wed:
                checkBox = (CheckBox) findViewById(checkboxs[3]);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                break;

            case R.id.week_of_day_thu:
                checkBox = (CheckBox) findViewById(checkboxs[4]);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                break;

            case R.id.week_of_day_fri:
                checkBox = (CheckBox) findViewById(checkboxs[5]);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                break;

            case R.id.week_of_day_sat:
                checkBox = (CheckBox) findViewById(checkboxs[6]);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
                break;

            default:
                break;
        }
    }

    public void getDataFromBox() {

        CheckBox checkBox;
        weekday_bit = 0;
        int idx = 1;
        for (int box : checkboxs) {
            checkBox = (CheckBox) findViewById(box);
            if (checkBox.isChecked()) {
                weekday_bit |= (1 << idx);
            }
            idx++;
        }
    }

    public void setCheckState() {
        CheckBox checkBox;
        for (int i = 1; i < 8; i++) {
            int flag = Utills.checkTargetWeekOfDayIsSet(weekday_bit, i);
            if (flag != 0) {
                checkBox = (CheckBox) findViewById(checkboxs[i-1]);
                checkBox.setChecked(true);
            }
        }
    }
}
