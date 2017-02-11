package com.inkp.boostcamp.Boostme.activities;

import android.content.DialogInterface;
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
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

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


    public int orders;
    public long MainScheduleId;

    public Date Dates;
    public String Title;
    public int WeekOfDays;
    public String Location;
    public boolean MainAlarm = true;
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

    @BindView(R.id.add_small_task_button)
    ImageView addSmallTaskButton;
    @BindView(R.id.add_load_small_task_button)
    ImageView loadSmallTaskButton;

    Realm realm;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_add_task);
        ButterKnife.bind(this);

        orders = 0;
        Dates = new Date();

        smallSchedules = new ArrayList<>();
        AddSmallTask("First", new Date(), -1);
        AddSmallTask("Second", new Date(), -2);
        Log.d("SIZE", String.valueOf(smallSchedules.size()));

        realm = Realm.getDefaultInstance();
        MainScheduleId = getNextKeyMainSchedule(realm);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));



        smallScheduleRecyclerView = (RecyclerView) findViewById(R.id.add_addtask_recycler_view);

        smallScheduleAdapter = new SmallScheduleAdapter(smallSchedules);
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
                        Toast.makeText(getBaseContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
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

                        InsertSchduleToDatabase();
                        //addSchdule();
                        //refreshView();

                    }
                }
        );

        //final ImageView saveListButton = (ImageView) view.findViewById(R.id.add_save_button);
        return true;
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
        //메인 fragments 갱신 필요
        //포커싱 될 때마다 갱신을 하던, 할 것
    }

    //RecyclerView로 전달하기 위한 일시적인 small Task Array list
    //Database에 들어가는 객체는
    public void AddSmallTask(String sTitle, Date sDates, int Order){
        SmallSchedule newTask = new SmallSchedule();
        newTask.setSmall_tilte(sTitle);
        newTask.setSmall_time(sDates);
        newTask.setOrder_value(Order);
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
                int h, m;
                h=timePicker.getCurrentHour();
                m=timePicker.getCurrentMinute();
                Date tempDate = new Date();
                tempDate.setHours(h);
                tempDate.setMinutes(m);
                AddSmallTask(edit_title.getText().toString(), tempDate, orders++);

                smallScheduleAdapter.dataChagned(smallSchedules);
                smallScheduleAdapter.notifyDataSetChanged();
                smallScheduleRecyclerView.setAdapter(smallScheduleAdapter);
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
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_datetime, null);

        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.add_task_timepicker);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.add_task_datepicker);

        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setView(dialogView);
        buider.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Dates.setHours(timePicker.getCurrentHour());
                Dates.setMinutes(timePicker.getCurrentMinute());
                Dates.setYear(datePicker.getYear());
                Dates.setMonth(datePicker.getMonth());
                Dates.setDate(datePicker.getDayOfMonth());

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, hh:mm a");
                dateView.setText(sdf.format(Dates).toString());
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

    public void smallScheduleAddToRealm(){
        for(int i = 0; i<smallSchedules.size(); i++){
            final int idx = i;
            final int sId = getNextKeySmallSchedule(realm);

            realm.executeTransactionAsync(new Realm.Transaction(){
                @Override
                public void execute(Realm bgRealm){
                    SmallScheduleRealm smallScheduleRealm = bgRealm.createObject(SmallScheduleRealm.class, sId);

                    SmallSchedule dataForTransaction = smallSchedules.get(idx);
                    smallScheduleRealm.setSchedule_id(MainScheduleId);
                    smallScheduleRealm.setSmall_tilte(dataForTransaction.getSmall_tilte());
                    smallScheduleRealm.setSmall_time(dataForTransaction.getSmall_time());
                    smallScheduleRealm.setOrder_value(dataForTransaction.getOrder_value());
                    smallScheduleRealm.setAlarm_flag(dataForTransaction.isAlarm_flag());

                    //Realm realmForQuery = Realm.getDefaultInstance();
                    ScheduleRealm mainSchedule = bgRealm.where(ScheduleRealm.class).equalTo("id", MainScheduleId).findFirst();
                    mainSchedule.getSmall_schedule().add(smallScheduleRealm);
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
            }}, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess(){
                Log.d("REALM", "Data Sucess");
            }
        });
    }



}
