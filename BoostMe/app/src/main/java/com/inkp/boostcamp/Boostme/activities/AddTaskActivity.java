package com.inkp.boostcamp.Boostme.activities;

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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.SmallScheduleAdapter;
import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

import static com.inkp.boostcamp.Boostme.Utills.format_yymmdd_hhmm_a;
import static com.inkp.boostcamp.Boostme.Utills.getNextKeyMainSchedule;
import static com.inkp.boostcamp.Boostme.Utills.getNextKeySmallSchedule;

/**
 * Created by macbook on 2017. 2. 9..
 */


//UUID.randomUUID().toString();

public class AddTaskActivity extends AppCompatActivity{

    public SmallScheduleAdapter smallScheduleAdapter;
    public RecyclerView smallScheduleRecyclerView;
    public ArrayList<SmallSchedule> smallSchedules;


    public SmallSchedule departSchedule;
    public SmallSchedule finalSchedule;

    public long MainScheduleId;

    public Date Dates;
    public String Title;
    public int WeekOfDays;
    public String Location;
    public boolean MainAlarm = true;
    public boolean isDepartTimeSet = false;
    //list에 대해서 -> 배열을 통해 객체를 따로 넣는다
    //해당 리스트는 타이틀 시간 우선순위 알람여부를 갖는 하위 데이터 테이블을 갖는다


    @BindView(R.id.add_task_title)
    TextView titleView;
    @BindView(R.id.add_task_date)
    TextView dateView;
    @BindView(R.id.add_task_time)
    TextView timeView;
    @BindView(R.id.add_location)
    TextView locationView;
    @BindView(R.id.add_datetime_linear)
    LinearLayout datetime_linearViewer;
    @BindView(R.id.add_week_of_day_repeat)
    TextView weekofdaysView;

    @BindView(R.id.add_small_task_button)
    ImageView addSmallTaskButton;
    @BindView(R.id.add_load_small_task_button)
    ImageView loadSmallTaskButton;
    @BindView(R.id.add_location_depart_time)
    TextView locationDepartTimeView;

    Realm realm;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_add_task);
        ButterKnife.bind(this);

        Dates = new Date();
        Dates.setSeconds(0);
        WeekOfDays = 0;

        //출발시간 & 마지막 스케쥴 초기화
        departSchedule = new SmallSchedule();
        finalSchedule = new SmallSchedule();
        Date finalTime= new Date(); finalTime.setTime(0);
        finalSchedule.setSmall_time(finalTime);
        finalSchedule.setSmall_tilte("일정 시작");


        dateView.setText(format_yymmdd_hhmm_a.format(new Date()));
        smallSchedules = new ArrayList<>();

        //realm database setup
        realm = Realm.getDefaultInstance();
        MainScheduleId = getNextKeyMainSchedule(realm);

        //action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));


        //recyclerView part
        smallScheduleRecyclerView = (RecyclerView) findViewById(R.id.add_addtask_recycler_view);

        smallScheduleAdapter = new SmallScheduleAdapter(smallSchedules, Dates, departSchedule, finalSchedule);
        smallScheduleRecyclerView.hasFixedSize();
        smallScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        smallScheduleRecyclerView.setAdapter(smallScheduleAdapter);


        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(smallScheduleAdapter, true, true, true);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(smallScheduleRecyclerView);

        smallScheduleRecyclerView.addItemDecoration(
                new RVHItemDividerDecoration(this, LinearLayoutManager.VERTICAL));

        smallScheduleRecyclerView.addOnItemTouchListener(
                new RVHItemClickListener(this, new RVHItemClickListener.OnItemClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {
                        SmallSchedule temp = smallScheduleAdapter.printOnclick(position);
                        Toast.makeText(getBaseContext(), temp.getSmall_tilte() + ", " + temp.getOrder_value(), Toast.LENGTH_SHORT).show();
                    }
                })
        );


        addSmallTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogForSmallTasks();

            }
        });
        datetime_linearViewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddTaskActivity.this, "hi", Toast.LENGTH_SHORT).show();
                CustomDialogForDateTime();
            }
        });
        weekofdaysView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(AddTaskActivity.this, SelectWeekdaysActivity.class);
                intent.putExtra("curVal", WeekOfDays);
                startActivityForResult(intent, Utills.weekdays_requestCode);
            }
        });

        locationView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                CustomDialog_location();
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
                        if(smallSchedules.isEmpty()){
                            Toast.makeText(AddTaskActivity.this, "최소 하나의 세부 일정이 필요합니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else
                            InsertSchduleToDatabase();
                        //addSchdule();

                    }
                }
        );
        //final ImageView saveListButton = (ImageView) view.findViewById(R.id.add_save_button);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Utills.weekdays_resultCode){
            WeekOfDays = data.getIntExtra("weekdaysVal", -1);
            if(WeekOfDays == -1){
                Toast.makeText(this, "ERROR : weekdaysVal is illegal value", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(WeekOfDays > 0){
                setWeekDayToView(WeekOfDays);
            }
            Toast.makeText(this, String.valueOf(WeekOfDays), Toast.LENGTH_SHORT).show();

        }

    }


    public void getDataFromView(){
        Title = titleView.getText().toString();
        //Dates, Week_of_Days_repeats는 사용자 설정시 즉시 반영된다
        Location = locationView.getText().toString();

    }


    private void InsertSchduleToDatabase(){
        getDataFromView();
        mainScheduleAddtoRealm();
        smallScheduleAddToRealm();
        finish();
    }

    //RecyclerView로 전달하기 위한 일시적인 small Task Array list
    //Database에 들어가는 객체는
    public void AddSmallTask(String sTitle, Date sDates){
        SmallSchedule newTask = new SmallSchedule();
        newTask.setSmall_tilte(sTitle);
        newTask.setSmall_time(sDates);
        smallSchedules.add(newTask);
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
        //buider.setTitle("Member Information"); //Dialog 제목
        buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
        buider.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ArrayList<SmallSchedule> newSchedule = new ArrayList<SmallSchedule>();
                newSchedule.addAll(smallScheduleAdapter.getSmallSchedules());
                smallSchedules.clear();
                smallSchedules.addAll(newSchedule);

                int h, m;
                h=timePicker.getCurrentHour();
                m=timePicker.getCurrentMinute();
                Date tempDate = new Date();
                tempDate.setHours(h);
                tempDate.setMinutes(m);
                String s_title = edit_title.getText().toString();
                if(s_title.length() == 0) {
                    Toast.makeText(AddTaskActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                }

                if(isDepartTimeSet) {
                    smallSchedules.remove(smallSchedules.size() - 1);
                    AddSmallTask(s_title, tempDate);
                    smallSchedules.add(departSchedule);
                }else{
                    AddSmallTask(s_title, tempDate);
                }
                refreshRecyclerView(smallSchedules, departSchedule);
            }
        });
        buider.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog dialog = buider.create();
        dialog.setCanceledOnTouchOutside(false);


        dialog.show();

    }

    public void CustomDialogForDateTime() {
        //시간 변경된거 Apdater에 넣어줘야함
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_datetime, null);

        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.add_task_timepicker);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.add_task_datepicker);

        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setView(dialogView);
        buider.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ArrayList<SmallSchedule> newSchedule = new ArrayList<SmallSchedule>();
                newSchedule = smallScheduleAdapter.getSmallSchedules();
                smallSchedules.clear();
                smallSchedules.addAll(newSchedule);
                refreshRecyclerView(smallSchedules, departSchedule);

                Dates.setHours(timePicker.getCurrentHour());
                Dates.setMinutes(timePicker.getCurrentMinute());
                Dates.setYear(datePicker.getYear());
                Dates.setMonth(datePicker.getMonth());
                Dates.setDate(datePicker.getDayOfMonth());

                dateView.setText(Utills.format_yymmdd_hhmm_a.format(Dates).toString());
            }

        });
        buider.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });
        AlertDialog dialog = buider.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


    public void CustomDialog_location(){
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_location, null);
        final TextView locationName = (TextView) dialogView.findViewById(R.id.dialog_location);

        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.dialog_location_time);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(0);
        timePicker.setCurrentMinute(0);


        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setView(dialogView);
        buider.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Location = locationName.getText().toString();

                int h, m;
                h=timePicker.getCurrentHour();
                m=timePicker.getCurrentMinute();
                Date tempDate = new Date();
                tempDate.setHours(h);
                tempDate.setMinutes(m);
                departSchedule.setSmall_time(tempDate);
                departSchedule.setSmall_tilte("출발");
                locationDepartTimeView.append(String.valueOf(tempDate.getMinutes() + tempDate.getHours() * 60) + "분");

                if(isDepartTimeSet) {
                    smallSchedules.remove(smallSchedules.size() - 1);
                    smallSchedules.add(departSchedule);
                }else{
                    smallSchedules.add(departSchedule);
                    isDepartTimeSet=true;
                }

                refreshRecyclerView(smallSchedules, departSchedule);
            }
        });
        buider.setNegativeButton("취소", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
            }
        });
        AlertDialog dialog = buider.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void smallScheduleAddToRealm(){
        for(int i = 0; i<smallSchedules.size(); i++){
            final int idx = i;

            realm.executeTransactionAsync(new Realm.Transaction(){
                @Override
                public void execute(Realm bgRealm){
                    SmallScheduleRealm smallScheduleRealm = bgRealm.createObject(SmallScheduleRealm.class, getNextKeySmallSchedule(bgRealm));

                    SmallSchedule dataForTransaction = smallSchedules.get(idx);
                    smallScheduleRealm.setSchedule_id(MainScheduleId);
                    smallScheduleRealm.setSmall_tilte(dataForTransaction.getSmall_tilte());
                    smallScheduleRealm.setSmall_time(dataForTransaction.getSmall_time());
                    smallScheduleRealm.setOrder_value(dataForTransaction.getOrder_value());
                    smallScheduleRealm.setAlarm_flag(dataForTransaction.isAlarm_flag());

                    //Realm realmForQuery = Realm.getDefaultInstance();
                    ScheduleRealm mainSchedule = bgRealm.where(ScheduleRealm.class).equalTo("id", MainScheduleId).findFirst();
                    mainSchedule.getSmall_schedule().add(smallScheduleRealm);
                    bgRealm.insertOrUpdate(smallScheduleRealm);
                }
            });
        }
    }

    public void mainScheduleAddtoRealm(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                ScheduleRealm schedule = bgRealm.createObject(ScheduleRealm.class, MainScheduleId);
                schedule.setTitle(Title);
                schedule.setDate(Dates);
                schedule.setLocation(Location);
                schedule.setWeek_of_day_repit(WeekOfDays);
                schedule.setAlarm_flag(MainAlarm);
                bgRealm.insertOrUpdate(schedule);
            }}, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess(){
                Log.d("REALM", "Data Sucess");
            }
        });
    }


    public void setWeekDayToView(int val) {
        weekofdaysView.setText("");
        //init textView
        for (int i = 1; i < 8; i++) {


            int flag = Utills.checkTargetWeekOfDayIsSet(val, i);
            if(flag != 0){
                switch (i){
                    case 1 :
                        weekofdaysView.append("일 ");
                        break;
                    case 2 :
                        weekofdaysView.append("월 ");
                        break;
                    case 3 :
                        weekofdaysView.append("화 ");
                        break;
                    case 4 :
                        weekofdaysView.append("수 ");
                        break;
                    case 5 :
                        weekofdaysView.append("목 ");
                        break;
                    case 6 :
                        weekofdaysView.append("금 ");
                        break;
                    case 7 :
                        weekofdaysView.append("토 ");
                        break;
                }
            }


        }
    }


    public void refreshRecyclerView(ArrayList<SmallSchedule> newData, SmallSchedule depart){

        smallScheduleAdapter.dataChagned(newData);
        smallScheduleAdapter.notifyDataSetChanged();
    }

    public void setScheduleStartTime(String title, Date date){

        //departTime => 설정 여부 체크 *
        //미설정시 일정 시작 시간만

        //등록되는 시간을 list의 마지막에 추가할 것.
        //Adpater 생성자에 리스트를 하나 더 던진다

        //시간 갱신은 사건 순으로
        //등록되는 순으로
        //for문으로 total time 구한 뒤, 기준 시간으로부터 시간 감소시켜 누적 시간 값 구할 것
        //drag&drop 값 갱신
    }

}
