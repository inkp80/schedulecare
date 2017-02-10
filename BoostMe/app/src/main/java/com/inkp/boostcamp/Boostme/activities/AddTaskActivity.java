package com.inkp.boostcamp.Boostme.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.RobotoCalendarView;
import com.inkp.boostcamp.Boostme.ScheduleAdapter;
import com.inkp.boostcamp.Boostme.SmallScheduleAdapter;
import com.inkp.boostcamp.Boostme.data.Schedule;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;

/**
 * Created by macbook on 2017. 2. 9..
 */


//UUID.randomUUID().toString();

public class AddTaskActivity extends AppCompatActivity{

    public SmallScheduleAdapter smallScheduleAdapter;
    public RecyclerView smallScheduleRecyclerView;
    public ArrayList<SmallSchedule> smallSchedules;


    public int orders;
    public String GUID;

    public int year, month, date, hour, minute;

    public Date Dates;
    public String Title;
    public int WeekOfDays;
    public String Location;
    //list에 대해서 -> 배열을 통해 객체를 따로 넣는다
    //해당 리스트는 타이틀 시간 우선순위 알람여부를 갖는 하위 데이터 테이블을 갖는다


    @BindView(R.id.add_task_title)
    TextView titleView;
    @BindView(R.id.add_task_date)
    TextView dateView;
    @BindView(R.id.add_task_time)
    TextView timeView;

    @BindView(R.id.add_small_task_button)
    ImageView addSmallTaskButton;
    @BindView(R.id.load_small_task_button)
    ImageView loadSmallTaskButton;

    Realm realm;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_add_task);
        ButterKnife.bind(this);

        orders = 0;
        GUID = UUID.randomUUID().toString();

        smallSchedules = new ArrayList<>();
        AddSmallTask("First", new Date(), -1);
        Log.d("SIZE", String.valueOf(smallSchedules.size()));

        realm = Realm.getDefaultInstance();


        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));



        smallScheduleRecyclerView = (RecyclerView) findViewById(R.id.add_task_recycler_view);

        smallScheduleAdapter = new SmallScheduleAdapter(smallSchedules);
        smallScheduleRecyclerView.hasFixedSize();
        smallScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        smallScheduleRecyclerView.setAdapter(smallScheduleAdapter);

        addSmallTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogForSmallTasks();
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        realm.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar_add, null);

        actionBar.setCustomView(actionbar);
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        View view = getSupportActionBar().getCustomView();
        ImageView saveButton = (ImageView) view.findViewById(R.id.add_save_button);
        saveButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){

                        //addSchdule();
                        //refreshView();

                    }
                }
        );

        final ImageView saveListButton = (ImageView) view.findViewById(R.id.add_save_button);

        return true;
    }


    public void getDataFromView(){

        Title = titleView.getText().toString();

    }


    RealmAsyncTask transaction;

    private void InsertSchduleToDatabase(final String title, final Date date, final String location){

        transaction = realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(Realm bgRealm) {
                Schedule schedule = bgRealm.createObject(Schedule.class);
                schedule.setTitle(title);
                schedule.setDate(date);
                schedule.setLocation(location);
                //schedule.setWeek_of_day_repit();
        }});
    }

    //RecyclerView로 전달하기 위한 일시적인 small Task Array list
    //Database에 들어가는 객체는
    public void AddSmallTask(String sTitle, Date sDates, int Order){
        SmallSchedule newTask = new SmallSchedule();
        newTask.setSmall_tilte(sTitle);
        newTask.setSmall_time(sDates);
        newTask.setOrder_value(Order);
        smallSchedules.add(newTask);
        //week of days
        //location
    }

    public void CustomDialogForSmallTasks() {

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        final EditText edit_title = (EditText) dialogView.findViewById(R.id.dialog_small_title);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.dialog_small_timer);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(0);
        timePicker.setCurrentMinute(0);


        //멤버의 세부내역 입력 Dialog 생성 및 보이기
        AlertDialog.Builder buider = new AlertDialog.Builder(this); //AlertDialog.Builder 객체 생성
        buider.setTitle("Member Information"); //Dialog 제목
        buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
        buider.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int h, m;
                h=timePicker.getCurrentHour();
                m=timePicker.getCurrentMinute();
                Date tempDate = new Date();
                tempDate.setHours(h);
                tempDate.setMinutes(m);
                AddSmallTask(edit_title.getText().toString(), tempDate, orders++);
                for(int i=0; i<smallSchedules.size(); i++) {
                    Log.d("list SIZE", smallSchedules.get(i).getSmall_tilte());
                }

                smallScheduleAdapter.dataChagned(smallSchedules);
                smallScheduleAdapter.notifyDataSetChanged();
                smallScheduleRecyclerView.setAdapter(smallScheduleAdapter);
                //refreshView();

            }
        });
        buider.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog dialog = buider.create();

        //Dialog의 바깥쪽을 터치했을 때 Dialog를 없앨지 설정
        dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정

        //Dialog 보이기
        dialog.show();

    }

    public void CustomDialogForDateTime() {


    }


/*
    public void refreshView(){
        SmallScheduleAdapter SmallScheduleAdapter = new SmallScheduleAdapter(smallSchedules);
        smallScheduleRecyclerView.hasFixedSize();
        smallScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        smallScheduleRecyclerView.setAdapter(SmallScheduleAdapter);
    }*/



}
