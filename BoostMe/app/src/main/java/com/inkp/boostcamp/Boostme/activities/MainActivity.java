package com.inkp.boostcamp.Boostme.activities;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.inkp.boostcamp.Boostme.TabAdapter;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    int MAX_PAGE=2;
    //android.app.Fragment CalendarFragment;
    //android.app.Fragment EventFragment;

    long backKeyTime = 0;

    @BindView(R.id.toolbar_main_tag)
    ImageButton mTagListActivity;
    @BindView(R.id.toolbar_main_add)
    ImageButton mAddListButton;
    @BindView(R.id.toolbar_main_setup)
    ImageButton mSettingButton;

    Fragment mCurFragment = new Fragment();
    Fragment mEventFragment = new MainEventFragment();
    Fragment mCalendarFragment = new MainCalendarFragment();

    private TabAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(Color.WHITE);
        ButterKnife.bind(this);

        //CalendarFragment = getFragmentManager().findFragmentById(R.id.main_calendar_fragment);
        //EventFragment = getFragmentManager().findFragmentById(R.id.main_event_fragment);

        //setFragmentShow(CalendarFragment, EventFragment);

       /* mEventListFragment.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurFragment = mEventFragment;
                        //setFragmentShow(EventFragment, CalendarFragment);
                    }
                }
        );

        mCalendarListFragment.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurFragment = mCalendarFragment;
                        //setFragmentShow(CalendarFragment, EventFragment);
                    }
                }
        );*/

        mTagListActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowTagActivity.class);
                startActivity(intent);
            }
        });


        mAddListButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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



        //ViewPager viewPager=(ViewPager)findViewById(R.id.main_viewpager);        //Viewpager 선언 및 초기화
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        mAdapter = new TabAdapter(getSupportFragmentManager());
        mAdapter.addFragment(new MainCalendarFragment());
        mAdapter.addFragment(new MainEventFragment());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(R.drawable.calendar_white);
        mTabLayout.getTabAt(1).setIcon(R.drawable.event_list_icon);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mTabLayout.addOnTabSelectedListener(this);

       // viewPager.setAdapter(new adapter(getSupportFragmentManager()));     //선언한 viewpager에 adapter를 연결

        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));
    }



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

    public void setFragmentShow(android.app.Fragment fragmentToShow, android.app.Fragment fragmentToHide) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.show(fragmentToShow);
        fragmentTransaction.hide(fragmentToHide);
        fragmentTransaction.commit();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
        switch (tab.getPosition()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;

        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

