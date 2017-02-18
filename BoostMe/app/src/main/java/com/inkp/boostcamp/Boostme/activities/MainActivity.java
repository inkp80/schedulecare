package com.inkp.boostcamp.Boostme.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.inkp.boostcamp.Boostme.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    android.app.Fragment CalendarFragment;
    android.app.Fragment EventFragment;

    long backKeyTime = 0;

    @BindView(R.id.toolbar_main_calendar)
    TextView mCalendarListFragment;
    @BindView(R.id.toolbar_main_event)
    TextView mEventListFragment;
    @BindView(R.id.toolbar_main_add)
    ImageButton mAddListButton;
    @BindView(R.id.toolbar_main_setup)
    ImageButton mSettingButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(Color.WHITE);
        ButterKnife.bind(this);

        CalendarFragment = getFragmentManager().findFragmentById(R.id.main_calendar_fragment);
        EventFragment = getFragmentManager().findFragmentById(R.id.main_event_fragment);

        setFragmentShow(CalendarFragment, EventFragment);

        mEventListFragment.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Event", Toast.LENGTH_SHORT).show();
                        setFragmentShow(EventFragment, CalendarFragment);
                    }
                }
        );

        mCalendarListFragment.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(MainActivity.this, "Calendar", Toast.LENGTH_SHORT).show();
                        setFragmentShow(CalendarFragment, EventFragment);
                    }
                }
        );


        mAddListButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                        startActivity(intent);
                    }
                }
        );

        mSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "To Setting", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar_main, null);

        actionBar.setCustomView(actionbar);
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);


        View view = getSupportActionBar().getCustomView();

        final ImageView EventListButton = (ImageView) view.findViewById(R.id.main_event_button);
        final ImageView CalendarListButton = (ImageView) view.findViewById(R.id.main_calendar_button);
        final ImageView AddListButton = (ImageView) view.findViewById(R.id.main_add_button);
        final ImageView SettingButton = (ImageView) view.findViewById(R.id.main_setting_button);

        EventListButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Event", Toast.LENGTH_SHORT).show();
                        CalendarListButton.setVisibility(View.VISIBLE);
                        EventListButton.setVisibility(View.INVISIBLE);
                        setFragmentShow(EventFragment, CalendarFragment);
                    }
                }
        );


        CalendarListButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(MainActivity.this, "Calendar", Toast.LENGTH_SHORT).show();
                        CalendarListButton.setVisibility(View.INVISIBLE);
                        EventListButton.setVisibility(View.VISIBLE);
                        setFragmentShow(CalendarFragment, EventFragment);
                    }
                }
        );

        AddListButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                        startActivity(intent);
                    }
                }
        );

        SettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "To Setting", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        return true;
    }*/

    @Override
    public void onBackPressed() {
        Toast toast;
        if (System.currentTimeMillis() > backKeyTime + 2000) {
            backKeyTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyTime + 2000) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void setFragmentShow(android.app.Fragment fragmentToShow, android.app.Fragment fragmentToHide){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.show(fragmentToShow);
        fragmentTransaction.hide(fragmentToHide);
        fragmentTransaction.commit();
    }
}
