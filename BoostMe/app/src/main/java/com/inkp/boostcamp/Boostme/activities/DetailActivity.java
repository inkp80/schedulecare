package com.inkp.boostcamp.Boostme.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.DetailScheduleAdapter;
import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.ScheduleParcel;
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.internal.Util;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by macbook on 2017. 2. 11..
 */

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_title)
    TextView detail_titleView;
    @BindView(R.id.detail_date)
    TextView detail_dateView;
    @BindView(R.id.detail_location)
    TextView detail_locationView;
    @BindView(R.id.detail_recyclerview)
    RecyclerView detail_recyclerView;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));
        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        long targetId = intent.getLongExtra(Utills.access_Schedule_id, -1);
        Log.d("####",String.valueOf(targetId));
        //Bundle bundle = getIntent().getExtras();
        //ScheduleParcel scheduleParcel = bundle.getParcelable("scheduleParcel");

        ScheduleRealm Schedules = realm.where(ScheduleRealm.class).equalTo("id", targetId).findFirst();
        Schedules.getSmall_schedule();

        Log.d("####", Schedules.getTitle());

        detail_titleView.setText(Schedules.getTitle());
        detail_dateView.setText(Utills.format_yymmdd_hhmm_a.format(Schedules.getDate()));
        //RealmList<SmallScheduleRealm> result = new RealmList<>();
        //result = Schedules.getSmall_schedule();

        RealmResults<SmallScheduleRealm> result = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", targetId).findAll();
        Log.d("###size#",String.valueOf(result.size()));
        DetailScheduleAdapter detailScheduleAdapter = new DetailScheduleAdapter(getBaseContext(), result);
        detail_recyclerView.hasFixedSize();
        detail_recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        detail_recyclerView.setAdapter(detailScheduleAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar_detail, null);

        actionBar.setCustomView(actionbar);
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);


        View view = getSupportActionBar().getCustomView();

        //final ImageView EventListButton = (ImageView) view.findViewById(R.id.main_event_button);

        /*EventListButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Event", Toast.LENGTH_SHORT).show();
                        CalendarListButton.setVisibility(View.VISIBLE);
                        EventListButton.setVisibility(View.INVISIBLE);
                        setFragmentShow(EventFragment, CalendarFragment);
                    }
                }
        );*/
        return true;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}