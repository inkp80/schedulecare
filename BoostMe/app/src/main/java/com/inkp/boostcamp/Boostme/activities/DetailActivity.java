package com.inkp.boostcamp.Boostme.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.DetailScheduleAdapter;
import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.ScheduleParcel;
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;
import com.inkp.boostcamp.Boostme.receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
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
    @BindView(R.id.detail_location_time)
    TextView detail_location_timeVIew;
    @BindView(R.id.detail_recyclerview)
    RecyclerView detail_recyclerView;

    @BindView(R.id.toolbar_detail_delete)
    ImageView mDeleteButton;
    @BindView(R.id.toolbar_detail_edit)
    ImageButton mEditButton;

    @BindView(R.id.detail_weekday_sun)
    TextView mDetailSunSelected;
    @BindView(R.id.detail_weekday_mon)
    TextView mDetailMonSelected;
    @BindView(R.id.detail_weekday_tue)
    TextView mDetailTueSelected;
    @BindView(R.id.detail_weekday_wed)
    TextView mDetailWedSelected;
    @BindView(R.id.detail_weekday_thu)
    TextView mDetailThuSelected;
    @BindView(R.id.detail_weekday_fri)
    TextView mDetailFriSelected;
    @BindView(R.id.detail_weekday_sat)
    TextView mDetailSatSelected;


    Realm realm;
    ScheduleRealm mScheduleObject;
    RealmResults<SmallScheduleRealm> mSmallScheduleObjectList;
    int targetId;
    public static int REQUEST_CODE = 134;
    public static int RESULT_CODE = 431;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.detail_toolbar));

        realm = Realm.getDefaultInstance();
        Intent intent = getIntent();
        targetId = intent.getIntExtra(Utills.access_Schedule_id, -1);

        mScheduleObject = realm.where(ScheduleRealm.class).equalTo("id", targetId).findFirst();
        mSmallScheduleObjectList = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", targetId).findAll();
        mSmallScheduleObjectList = mSmallScheduleObjectList.sort("order_value", Sort.ASCENDING);
        setWeekdayOnView(mScheduleObject.getWeek_of_day_repit());

        detail_titleView.setText(mScheduleObject.getTitle());
        if(mScheduleObject.getWeek_of_day_repit()==0)
            detail_dateView.setText(Utills.format_yymmdd_hhmm_a.format(mScheduleObject.getDate()));
        else{
            detail_dateView.setText("요일 반복, "+Utills.format_a_hhmm.format(mScheduleObject.getDate()));
        }

        //RealmList<SmallScheduleRealm> result = new RealmList<>();
        //result = Schedules.getSmall_schedule();

        //RealmResults<SmallScheduleRealm> result = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", targetId).findAll();
        //result = result.sort("order_value", Sort.ASCENDING);

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(mSmallScheduleObjectList.get(mSmallScheduleObjectList.size()-1).getSmall_time().getTime());
        detail_location_timeVIew.setText("소요시간 "+ (calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY) * 60)+ " 분");
        detail_locationView.setText(mScheduleObject.getLocation());

        DetailScheduleAdapter detailScheduleAdapter = new DetailScheduleAdapter(getBaseContext(), mSmallScheduleObjectList, mScheduleObject);
        detail_recyclerView.hasFixedSize();
        detail_recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        detail_recyclerView.setAdapter(detailScheduleAdapter);

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        /*
                        for(int i=0; i<mSmallScheduleObjectList.size(); i++) {
                            int ALARM_ID = Utills.alarmIdBuilder(targetId, i);
                            Intent intentForCancle = new Intent(getBaseContext(), AlarmReceiver.class);
                            PendingIntent pendingIntentForCancle
                                    = PendingIntent.getBroadcast(getBaseContext(), ALARM_ID, intentForCancle, PendingIntent.FLAG_CANCEL_CURRENT);
                            AlarmReceiver.cancelAlarm(pendingIntentForCancle, getBaseContext());
                        }*/
                        Utills.cancleAlarm(getBaseContext(), targetId, mSmallScheduleObjectList);
                        mScheduleObject.deleteFromRealm(); // 비 직접적인 객체 삭제
                        mSmallScheduleObjectList.deleteAllFromRealm();
                        finish();
                    }
                });

            }
        });

        mEditButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddTaskActivity.class);
                Utills.cancleAlarm(getBaseContext(), targetId, mSmallScheduleObjectList);
                intent.putExtra("intentAction", Utills.INTENT_ACTION_EDIT_SCHEDULE);
                intent.putExtra(Utills.ALARM_intent_scheduleId, mScheduleObject.getId());
                intent.putExtra(Utills.ALARM_intent_title, mScheduleObject.getTitle());
                intent.putExtra(Utills.ALARM_intent_date, mScheduleObject.getDate_in_long());
                intent.putExtra(Utills.ALARM_intent_weekofday, mScheduleObject.getWeek_of_day_repit());

                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("intente", "received");
        if (requestCode == REQUEST_CODE) {
            if (requestCode == RESULT_CODE) {
                //mScheduleObject = realm.where(ScheduleRealm.class).equalTo("id", targetId).findFirst();
                mSmallScheduleObjectList = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", targetId).findAll();
                Log.d("intente", "result in");
                detail_titleView.setText(data.getStringExtra(Utills.ALARM_intent_title));

                if (data.getIntExtra(Utills.ALARM_intent_weekofday, 0) == 0) {
                    detail_dateView.setText(Utills.format_yymmdd_hhmm_a.format(new Date(data.getLongExtra(Utills.ALARM_intent_date, 0))));
                } else {
                    detail_dateView.setText("요일 반복, " + Utills.format_a_hhmm.format(new Date(data.getLongExtra(Utills.ALARM_intent_date, 0))));
                }

                //detail_dateView.setText(Utills.format_yymmdd_hhmm_a.format(new Date(data.getLongExtra(Utills.ALARM_intent_date,0))));
                setWeekdayOnView(data.getIntExtra(Utills.ALARM_intent_weekofday, 0));

                //RealmList<SmallScheduleRealm> result = new RealmList<>();
                //result = Schedules.getSmall_schedule();

                RealmResults<SmallScheduleRealm> result = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", targetId).findAll();
                result = result.sort("order_value", Sort.ASCENDING);


                DetailScheduleAdapter detailScheduleAdapter = new DetailScheduleAdapter(getBaseContext(), result, mScheduleObject);
                detail_recyclerView.hasFixedSize();
                detail_recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                detail_recyclerView.setAdapter(detailScheduleAdapter);
            }else
                return;

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public void setWeekdayOnView(int val) {
        for (int i = 1; i < 8; i++) {
            int flag = Utills.checkTargetWeekOfDayIsSet(val, i);
            if (flag != 0) {
                switch (i) {
                    case 1:
                        if (Build.VERSION.SDK_INT >= 16)
                            mDetailSunSelected.setBackground(getResources().getDrawable(R.drawable.weekday_ring));
                        else
                            mDetailSunSelected.setTextColor(getResources().getColor(R.color.selected_object));
                        break;
                    case 2:
                        if (Build.VERSION.SDK_INT >= 16)
                            mDetailMonSelected.setBackground(getResources().getDrawable(R.drawable.weekday_ring));
                        else
                            mDetailMonSelected.setTextColor(getResources().getColor(R.color.selected_object));
                        break;
                    case 3:
                        if (Build.VERSION.SDK_INT >= 16)
                            mDetailTueSelected.setBackground(getResources().getDrawable(R.drawable.weekday_ring));
                        else
                            mDetailTueSelected.setTextColor(getResources().getColor(R.color.selected_object));
                        break;
                    case 4:
                        if (Build.VERSION.SDK_INT >= 16)
                            mDetailWedSelected.setBackground(getResources().getDrawable(R.drawable.weekday_ring));
                        else
                            mDetailWedSelected.setTextColor(getResources().getColor(R.color.selected_object));
                        break;
                    case 5:
                        if (Build.VERSION.SDK_INT >= 16)
                            mDetailThuSelected.setBackground(getResources().getDrawable(R.drawable.weekday_ring));
                        else
                            mDetailThuSelected.setTextColor(getResources().getColor(R.color.selected_object));
                        break;
                    case 6:
                        if (Build.VERSION.SDK_INT >= 16)
                            mDetailFriSelected.setBackground(getResources().getDrawable(R.drawable.weekday_ring));
                        else
                            mDetailFriSelected.setTextColor(getResources().getColor(R.color.selected_object));
                        break;
                    case 7:
                        if (Build.VERSION.SDK_INT >= 16)
                            mDetailSatSelected.setBackground(getResources().getDrawable(R.drawable.weekday_ring));
                        else
                            mDetailSatSelected.setTextColor(getResources().getColor(R.color.selected_object));
                        break;
                }
            }
        }
    }

}