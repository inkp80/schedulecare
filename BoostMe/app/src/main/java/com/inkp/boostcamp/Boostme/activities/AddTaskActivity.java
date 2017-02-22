package com.inkp.boostcamp.Boostme.activities;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.SmallScheduleAdapter;
import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;
import com.inkp.boostcamp.Boostme.data.TagListRealm;
import com.inkp.boostcamp.Boostme.data.TagRealm;
import com.inkp.boostcamp.Boostme.receiver.AlarmReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.inkp.boostcamp.Boostme.Utills.format_yymmdd_hhmm_a;
import static com.inkp.boostcamp.Boostme.Utills.getNextKeyMainSchedule;
import static com.inkp.boostcamp.Boostme.Utills.getNextKeySmallSchedule;

/**
 * Created by macbook on 2017. 2. 9..
 */


//UUID.randomUUID().toString();

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private InputMethodManager imm;
    SmallScheduleAdapter smallScheduleAdapter;
    RecyclerView smallScheduleRecyclerView;
    List<SmallSchedule> smallSchedules;

    List<String> mTagTitles;
    RealmResults<TagRealm> mTag;

    SmallSchedule departSchedule;
    SmallSchedule finalSchedule;

    int action_flag = 0;

    int main_schedule_id;
    Calendar mCalendar;
    Date mDates;
    String mTitle;
    int mWeekOfDays;
    String mLocation;
    boolean mMainAlarm = true;
    boolean isDepartTimeSet = false;


    @BindView(R.id.add_task_title)
    TextView titleView;
    @BindView(R.id.add_task_date)
    TextView dateView;
    @BindView(R.id.add_location)
    EditText locationView;
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
    @BindView(R.id.add_location_icon_auto)
    ImageButton mLocationIcon_AutoCompleate;

    @BindView(R.id.toolbar_add_save)
    ImageButton mSaveListButton;


    @BindView(R.id.add_weekday_sun)
    TextView mWeekday_sun;
    @BindView(R.id.add_weekday_mon)
    TextView mWeekday_mon;
    @BindView(R.id.add_weekday_tue)
    TextView mWeekday_tue;
    @BindView(R.id.add_weekday_wed)
    TextView mWeekday_wed;
    @BindView(R.id.add_weekday_thu)
    TextView mWeekday_thu;
    @BindView(R.id.add_weekday_fri)
    TextView mWeekday_fri;
    @BindView(R.id.add_weekday_sat)
    TextView mWeekday_sat;


    Realm realm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        setSupportActionBar((Toolbar) findViewById(R.id.add_toolbar));


        //realm database setup
        realm = Realm.getDefaultInstance();
        main_schedule_id = getNextKeyMainSchedule(realm);
        mTag = realm.where(TagRealm.class).findAll();
        mTagTitles = new ArrayList<>();
        for (int i = 0; i < mTag.size(); i++) {
            mTagTitles.add(mTag.get(i).getTag_name());
        }

        mDates = new Date();
        mCalendar = new GregorianCalendar();
        mCalendar.setTime(mDates);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        mDates.setSeconds(0);
        mWeekOfDays = 0;

        Calendar tmp_calendar = Calendar.getInstance();
        tmp_calendar.set(Calendar.HOUR_OF_DAY, 0);
        tmp_calendar.set(Calendar.MINUTE, 0);
        smallSchedules = new ArrayList<>();
        departSchedule = new SmallSchedule();
        departSchedule.setDepart_time(true);
        departSchedule.setSmall_tilte("출발");
        departSchedule.setSmall_time(new Date(tmp_calendar.getTimeInMillis()));
        departSchedule.setSmall_time_long(tmp_calendar.getTimeInMillis());
        departSchedule.setAlarm_flag(true);
        smallSchedules.add(departSchedule);


        //Detail -> Add로 편집 요청이 들어왔다면..
        Intent intent = getIntent();
        String check_start_act = intent.getStringExtra("intentAction");
        if (check_start_act != null && check_start_act.equals(Utills.INTENT_ACTION_EDIT_SCHEDULE)) {
            titleView.setText(intent.getStringExtra(Utills.ALARM_intent_title));
            mCalendar.setTimeInMillis(intent.getLongExtra(Utills.ALARM_intent_date, 0));
            main_schedule_id = intent.getIntExtra(Utills.ALARM_intent_scheduleId, 0);
            mWeekOfDays = intent.getIntExtra(Utills.ALARM_intent_weekofday, 0);
            setWeekCheckState();
            setWeekDayToView(mWeekOfDays);
            smallSchedules.clear();
            RealmResults<SmallScheduleRealm> small_objects = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", main_schedule_id).findAll();
            for (int i = 0; i < small_objects.size(); i++) {
                SmallSchedule tmp = new SmallSchedule();
                tmp.setSmall_tilte(small_objects.get(i).getSmall_tilte());
                tmp.setSmall_time_long(small_objects.get(i).getSmall_time().getTime());
                tmp.setSmall_time(small_objects.get(i).getSmall_time());
                tmp.setAlarm_flag(small_objects.get(i).isAlarm_flag());
                smallSchedules.add(tmp);
            }
            mDates.setTime(mCalendar.getTimeInMillis());
            action_flag = 1;
        }


        dateView.setText(format_yymmdd_hhmm_a.format(mDates));

        smallScheduleRecyclerView = (RecyclerView) findViewById(R.id.add_addtask_recycler_view);
        //smallScheduleRecyclerView.

        smallScheduleAdapter = new SmallScheduleAdapter(smallSchedules, mDates, mCalendar.getTimeInMillis(), departSchedule, finalSchedule);
        smallScheduleRecyclerView.hasFixedSize();
        smallScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        smallScheduleRecyclerView.setAdapter(smallScheduleAdapter);


        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(smallScheduleAdapter, true, true, true);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(smallScheduleRecyclerView);

        smallScheduleRecyclerView.addItemDecoration(
                new RVHItemDividerDecoration(this, LinearLayoutManager.VERTICAL));

        smallScheduleRecyclerView.addOnItemTouchListener(
                new RVHItemClickListener(this, new RVHItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //SmallSchedule temp = smallScheduleAdapter.printOnclick(position);
                        //Toast.makeText(getBaseContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();

                        smallScheduleAdapter.notifyDataSetChanged();
                        boolean isAlarm = smallSchedules.get(position).isAlarm_flag();
                        if (isAlarm) {
                            smallSchedules.get(position).setAlarm_flag(false);
                        } else {
                            smallSchedules.get(position).setAlarm_flag(true);
                        }
                        smallScheduleAdapter.notifyDataSetChanged();
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
        weekofdaysView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTaskActivity.this, SelectWeekdaysActivity.class);
                intent.putExtra("curVal", mWeekOfDays);
                startActivityForResult(intent, Utills.weekdays_requestCode);
            }
        });

        /*locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog_location();
            }
        });*/
        locationDepartTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog_location();
            }
        });

        mSaveListButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (smallSchedules.isEmpty()) {
                            Toast.makeText(AddTaskActivity.this, "최소 하나의 세부 일정이 필요합니다.", Toast.LENGTH_SHORT).show();
                            return;
                        } else
                            InsertSchduleToDatabase();
                    }
                }
        );
        loadSmallTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelectOption();
            }
        });

        mLocationIcon_AutoCompleate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPlace(v);
            }
        });
        mWeekday_sun.setOnClickListener(this);
        mWeekday_mon.setOnClickListener(this);
        mWeekday_tue.setOnClickListener(this);
        mWeekday_wed.setOnClickListener(this);
        mWeekday_thu.setOnClickListener(this);
        mWeekday_fri.setOnClickListener(this);
        mWeekday_sat.setOnClickListener(this);
        initKeyBoard();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Utills.weekdays_requestCode) {

            if (resultCode == Utills.weekdays_resultCode) {
                mWeekOfDays = data.getIntExtra("weekdaysVal", -1);
                if (mWeekOfDays == -1) {
                    Toast.makeText(this, "ERROR : weekdaysVal is illegal value", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mWeekOfDays > 0) {
                    setWeekDayToView(mWeekOfDays);
                } else
                    weekofdaysView.setText("요일 반복");
                Toast.makeText(this, String.valueOf(mWeekOfDays), Toast.LENGTH_SHORT).show();
            }


            if (requestCode == Utills.GOOGLE_AUTOCOMPLETE_REQUESTCODE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    Log.e("TagRealm", "Place: " + place.getAddress() + place.getPhoneNumber() + place.getLatLng().latitude);
                    locationView.setText(place.getAddress().toString());
                    initKeyBoard();

                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    Log.e("TagRealm", status.getStatusMessage());

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
        } else
            return;
    }


    public void getDataFromView() {
        mTitle = titleView.getText().toString();
        //mDates, Week_of_Days_repeats는 사용자 설정시 즉시 반영된다
        mLocation = locationView.getText().toString();

    }

    private void InsertSchduleToDatabase() {
        getDataFromView();
        updateSmallSchedule();
        addAlarmRegister(main_schedule_id);
        mainScheduleAddtoRealm();
        smallScheduleAddToRealm();
        //Utills.alarmRegister(getBaseContext(), main_schedule_id);
        if (action_flag == 1) {
            Intent returnIntent = new Intent(getBaseContext(), DetailActivity.class);
            returnIntent.putExtra(Utills.ALARM_intent_title, mTitle);
            returnIntent.putExtra(Utills.ALARM_intent_date, mCalendar.getTimeInMillis());
            returnIntent.putExtra(Utills.ALARM_intent_weekofday, mWeekOfDays);
            setResult(DetailActivity.RESULT_CODE, returnIntent);
        }
        finish();
    }

    //RecyclerView로 전달하기 위한 일시적인 small Task Array list
    //Database에 들어가는 객체는
    public void AddSmallTask(String title, long datetime_long) {
        SmallSchedule newTask = new SmallSchedule();
        Date tmp = new Date(datetime_long);
        newTask.setSmall_tilte(title);
        newTask.setSmall_time(tmp);
        newTask.setSmall_time_long(datetime_long);
        newTask.setAlarm_flag(true);
        newTask.setDepart_time(false);
        smallSchedules.add(newTask);
    }

    List<SmallSchedule> newSchedule = new ArrayList<SmallSchedule>();

    public void CustomDialogForSmallTasks() {

        final Calendar calendar = Calendar.getInstance();
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        final EditText edit_title = (EditText) dialogView.findViewById(R.id.dialog_small_title);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.dialog_small_timer);
        timePicker.setIs24HourView(true);


        if (Build.VERSION.SDK_INT >= 23) {
            timePicker.setHour(0);
            timePicker.setMinute(0);
        } else {
            timePicker.setCurrentHour(0);
            timePicker.setCurrentMinute(0);
        }
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }
        });


        //멤버의 세부내역 입력 Dialog 생성 및 보이기
        AlertDialog.Builder buider = new AlertDialog.Builder(this); //AlertDialog.Builder 객체 생성
        //buider.setTitle("Member Information"); //Dialog 제목
        buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
        buider.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= 23) {
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());

                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                }

                newSchedule = null;
                newSchedule = new ArrayList<SmallSchedule>();
                newSchedule.addAll(smallScheduleAdapter.getSmallSchedules());
                smallSchedules = null;
                smallSchedules = new ArrayList<SmallSchedule>();
                smallSchedules.addAll(newSchedule);


                String s_title = edit_title.getText().toString();
                if (s_title.length() == 0) {
                    Toast.makeText(AddTaskActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if (isDepartTimeSet) {

                smallSchedules.remove(smallSchedules.size() - 1);
                AddSmallTask(s_title, calendar.getTimeInMillis());
                smallSchedules.add(departSchedule);

                /*} else {
                    AddSmallTask(s_title, calendar.getTimeInMillis());
                }
                */
                refreshRecyclerView(smallSchedules, departSchedule, mDates);
                imm.hideSoftInputFromWindow(edit_title.getWindowToken(), 0);

                Log.d("after ok", String.valueOf(calendar.getTime()));
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


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                mDates.setTime(mCalendar.getTimeInMillis());
                Log.d("mCalendar", String.valueOf(mCalendar.getTime()));
            }
        });
        datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DATE, dayOfMonth);
                mDates.setTime(mCalendar.getTimeInMillis());

            }
        });


        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setView(dialogView);
        buider.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("save", "press");
                newSchedule = null;
                newSchedule = new ArrayList<SmallSchedule>();
                newSchedule = smallScheduleAdapter.getSmallSchedules();

                smallSchedules = null;
                smallSchedules = new ArrayList<SmallSchedule>();
                smallSchedules.addAll(newSchedule);

                dateView.setText(Utills.format_yymmdd_hhmm_a.format(mDates).toString());

                refreshRecyclerView(smallSchedules, departSchedule, mDates);

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

    public void CustomDialog_location() {
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_location, null);
        //final TextView locationName = (TextView) dialogView.findViewById(R.id.dialog_location);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mCalendar.getTimeInMillis());
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.dialog_location_time);

        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= 23) {
            timePicker.setHour(0);
            timePicker.setMinute(0);
        } else {
            timePicker.setCurrentHour(0);
            timePicker.setCurrentMinute(0);
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                Log.d("location Cal", String.valueOf(calendar.getTime()));
            }
        });


        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setView(dialogView);
        buider.setPositiveButton("저장", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (Build.VERSION.SDK_INT >= 23) {
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());

                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                }

                newSchedule = null;
                newSchedule = new ArrayList<SmallSchedule>();
                newSchedule = smallScheduleAdapter.getSmallSchedules();
                smallSchedules = null;
                smallSchedules = new ArrayList<SmallSchedule>();
                smallSchedules.addAll(newSchedule);

                //mLocation = locationName.getText().toString();


                Date temp = new Date(calendar.getTimeInMillis());
                departSchedule.setSmall_time(temp);
                departSchedule.setSmall_time_long(calendar.getTimeInMillis());
                departSchedule.setSmall_tilte("출발");
                departSchedule.setDepart_time(true);
                departSchedule.setAlarm_flag(true);


                locationDepartTimeView.setText("소요시간 " + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)) + "분");

                //if (isDepartTimeSet && smallSchedules.size() != 0) {
                smallSchedules.remove(smallSchedules.size() - 1);
                smallSchedules.add(departSchedule);
                //} else {
                //    smallSchedules.add(departSchedule);
                //    isDepartTimeSet = true;
                //}

                refreshRecyclerView(smallSchedules, departSchedule, mDates);
            }
        });
        buider.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = buider.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void DialogSelectOption() {
        final String items[] = mTagTitles.toArray(new String[0]);


        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("Title");
        ab.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        setLoadTagIndex(whichButton);
                    }
                }).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        loadTag(getLoadTagIndex());
                        // OK 버튼 클릭시 , 여기서 선택한 값을 메인 Activity 로 넘기면 된다.
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Cancel 버튼 클릭시
                    }
                });
        ab.show();
    }

    public void smallScheduleAddToRealm() {

        if (action_flag == 1) {
            final RealmResults<SmallScheduleRealm> smallScheduleRealm = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", main_schedule_id).findAll();
            Utills.cancleAlarm(getBaseContext(), main_schedule_id, smallScheduleRealm);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    smallScheduleRealm.deleteAllFromRealm();
                }
            });
        }

        for (int i = 0; i < smallSchedules.size(); i++) {
            final int idx = i;

            //insert
            //smallScheduleRealm = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", main_schedule_id).equalTo("order_value", idx).findFirst();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    SmallScheduleRealm smallScheduleRealm = bgRealm.createObject(SmallScheduleRealm.class, getNextKeySmallSchedule(bgRealm));
                    SmallSchedule dataForTransaction = smallSchedules.get(idx);
                    smallScheduleRealm.setSchedule_id(main_schedule_id);
                    smallScheduleRealm.setSmall_tilte(dataForTransaction.getSmall_tilte());
                    smallScheduleRealm.setSmall_time(dataForTransaction.getSmall_time());
                    smallScheduleRealm.setAlarm_flag(dataForTransaction.isAlarm_flag());
                    smallScheduleRealm.setOrder_value(idx);
                    smallScheduleRealm.setAlarm_start_time(dataForTransaction.getAlert_time());

                    //Realm realmForQuery = Realm.getDefaultInstance();
                    //ScheduleRealm mainSchedule = bgRealm.where(ScheduleRealm.class).equalTo("id", main_schedule_id).findFirst();
                    //mainSchedule.getSmall_schedule().add(smallScheduleRealm);
                    bgRealm.insertOrUpdate(smallScheduleRealm);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("REALM", "small Data Sucess");
                }
            });
        }
    }


    public void mainScheduleAddtoRealm() {

        //insert
        if (action_flag == 0) {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {

                    ScheduleRealm schedule = bgRealm.createObject(ScheduleRealm.class, main_schedule_id);
                    schedule.setTitle(mTitle);
                    schedule.setDate(mDates);
                    schedule.setLocation(mLocation);
                    schedule.setWeek_of_day_repit(mWeekOfDays);
                    setWeekdaysRealm(schedule, mWeekOfDays);
                    schedule.setAlarm_flag(mMainAlarm);
                    schedule.setDate_in_long(mCalendar.getTimeInMillis());
                    bgRealm.insertOrUpdate(schedule);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("REALM", "main Data Sucess3");
                }
            });
        }
        //update
        else {
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    ScheduleRealm schedule = bgRealm.where(ScheduleRealm.class).equalTo("id", main_schedule_id).findFirst();
                    schedule.setTitle(mTitle);
                    schedule.setDate(mDates);
                    schedule.setLocation(mLocation);
                    schedule.setWeek_of_day_repit(mWeekOfDays);
                    setWeekdaysRealm(schedule, mWeekOfDays);
                    schedule.setAlarm_flag(mMainAlarm);
                    schedule.setDate_in_long(mCalendar.getTimeInMillis());
                    bgRealm.insertOrUpdate(schedule);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("REALM", "main Data Sucess");
                }
            });
        }

    }

    public void setWeekDayToView(int val) {
        resetWeekDaysView();
        weekofdaysView.setText("요일 반복 ");
        //init textView
        for (int i = 1; i < 8; i++) {
            int flag = Utills.checkTargetWeekOfDayIsSet(val, i);

            switch (i) {
                case 1:
                    if (flag != 0) {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_sun.setBackground(getResources().getDrawable(R.drawable.weekday_ring, getTheme()));
                        else
                            mWeekday_sun.setTextColor(getResources().getColor(R.color.selected_object));
                    } else {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_sun.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
                        else
                            mWeekday_sun.setTextColor(getResources().getColor(R.color.etc_menu_color));
                    }
                    break;
                case 2:
                    if (flag != 0) {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_mon.setBackground(getResources().getDrawable(R.drawable.weekday_ring, getTheme()));
                        else
                            mWeekday_mon.setTextColor(getResources().getColor(R.color.selected_object));
                    } else {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_mon.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
                        else
                            mWeekday_mon.setTextColor(getResources().getColor(R.color.etc_menu_color));
                    }
                    break;
                case 3:
                    if (flag != 0) {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_tue.setBackground(getResources().getDrawable(R.drawable.weekday_ring, getTheme()));
                        else
                            mWeekday_tue.setTextColor(getResources().getColor(R.color.selected_object));
                    } else {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_tue.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
                        else
                            mWeekday_tue.setTextColor(getResources().getColor(R.color.etc_menu_color));
                    }
                    break;
                case 4:
                    if (flag != 0) {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_wed.setBackground(getResources().getDrawable(R.drawable.weekday_ring, getTheme()));
                        else
                            mWeekday_wed.setTextColor(getResources().getColor(R.color.selected_object));
                    } else {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_wed.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
                        else
                            mWeekday_wed.setTextColor(getResources().getColor(R.color.etc_menu_color));
                    }
                    break;
                case 5:
                    if (flag != 0) {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_thu.setBackground(getResources().getDrawable(R.drawable.weekday_ring, getTheme()));
                        else
                            mWeekday_thu.setTextColor(getResources().getColor(R.color.selected_object));
                    } else {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_thu.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
                        else
                            mWeekday_thu.setTextColor(getResources().getColor(R.color.etc_menu_color));
                    }
                    break;
                case 6:
                    if (flag != 0) {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_fri.setBackground(getResources().getDrawable(R.drawable.weekday_ring, getTheme()));
                        else
                            mWeekday_fri.setTextColor(getResources().getColor(R.color.selected_object));
                    } else {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_fri.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
                        else
                            mWeekday_fri.setTextColor(getResources().getColor(R.color.etc_menu_color));
                    }
                    break;
                case 7:
                    if (flag != 0) {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_sat.setBackground(getResources().getDrawable(R.drawable.weekday_ring, getTheme()));
                        else
                            mWeekday_sat.setTextColor(getResources().getColor(R.color.selected_object));
                    } else {
                        if (Build.VERSION.SDK_INT >= 21)
                            mWeekday_sat.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
                        else
                            mWeekday_sat.setTextColor(getResources().getColor(R.color.etc_menu_color));
                    }
                    break;
            }


        }
    }

    public void resetWeekDaysView() {
        if (Build.VERSION.SDK_INT >= 21) {
            mWeekday_sun.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
            mWeekday_mon.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
            mWeekday_tue.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
            mWeekday_wed.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
            mWeekday_thu.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
            mWeekday_fri.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
            mWeekday_sat.setBackground(getResources().getDrawable(R.color.etc_menu_color, getTheme()));
        } else {
            mWeekday_sun.setTextColor(getResources().getColor(R.color.etc_menu_color));
            mWeekday_mon.setTextColor(getResources().getColor(R.color.etc_menu_color));
            mWeekday_tue.setTextColor(getResources().getColor(R.color.etc_menu_color));
            mWeekday_wed.setTextColor(getResources().getColor(R.color.etc_menu_color));
            mWeekday_thu.setTextColor(getResources().getColor(R.color.etc_menu_color));
            mWeekday_fri.setTextColor(getResources().getColor(R.color.etc_menu_color));
            mWeekday_sat.setTextColor(getResources().getColor(R.color.etc_menu_color));
        }
    }

    public void setWeekdaysRealm(ScheduleRealm obj, int val) {
        initWeekdays(obj);
        for (int i = 1; i < 8; i++) {
            int flag = Utills.checkTargetWeekOfDayIsSet(val, i);
            if (flag != 0) {
                switch (i) {
                    case 1:
                        obj.setSun(true);
                        break;
                    case 2:
                        obj.setMon(true);
                        break;
                    case 3:
                        obj.setTue(true);
                        break;
                    case 4:
                        obj.setWed(true);
                        break;
                    case 5:
                        obj.setThu(true);
                        break;
                    case 6:
                        obj.setFri(true);
                        break;
                    case 7:
                        obj.setSat(true);
                        break;
                }
            }
        }
    }

    public void initWeekdays(ScheduleRealm obj) {
        obj.setSun(false);
        obj.setMon(false);
        obj.setTue(false);
        obj.setWed(false);
        obj.setThu(false);
        obj.setFri(false);
        obj.setSat(false);
    }

    public void refreshRecyclerView(List<SmallSchedule> newData, SmallSchedule depart, Date newDate) {
        smallScheduleAdapter.dataChagned(newData, newDate);
    }

    private void initKeyBoard() {
        imm = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
    }

    public void addAlarmRegister(int schedule_id) {
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        long trigger_time = 0;
        int alarm_id = 0;
        Calendar calendar = Calendar.getInstance();
        Calendar current = Calendar.getInstance();
        //Log.d("alarm size", String.valueOf(smallSchedules.size()));

        for (int i = 0; i < smallSchedules.size(); i++) {

            if (smallSchedules.get(i).isAlarm_flag()) {
                trigger_time = smallSchedules.get(i).getAlert_time();

                alarm_id = Utills.alarmIdBuilder(schedule_id, i);

                //알람 필터링
                Date triger_date = new Date(trigger_time);
                calendar.setTime(triger_date);


                if ((current.getTimeInMillis() > trigger_time) && (mWeekOfDays == 0)) {
                    smallSchedules.get(i).setAlarm_flag(false);
                    Log.d("시간", "시간이 맞지 않음");
                    continue; //현재 시간보다 알림 시간이 이전 시간이면서, 동시에 요일 반복도 설정이 안되어 있으면 알람 등록하지 않음.
                }

                //메인 아이디, 메인 타이틀, 메인 시간, 세부 인덱스, 요일반복
                intent.putExtra(Utills.ALARM_intent_scheduleId, main_schedule_id); //메인아이디
                intent.putExtra(Utills.ALARM_intent_title, mTitle); //메인 타이틀
                intent.putExtra(Utills.ALARM_intent_date, mCalendar.getTimeInMillis()); //메인 시간
                intent.putExtra(Utills.ALARM_intent_scheduleIdx, i); //세부 인덱스
                intent.putExtra(Utills.ALARM_intent_weekofday, mWeekOfDays); //요일 반복
                intent.putExtra(Utills.ALARM_intent_small_title, smallSchedules.get(i).getSmall_tilte());

                PendingIntent pendingIntent
                        = PendingIntent.getBroadcast(getBaseContext(), alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Utills.enrollAlarm(getBaseContext(), pendingIntent, Utills.setTriggerTime(trigger_time));
                break;
            }
        }

    }

    public void updateSmallSchedule() {
        newSchedule = null;
        newSchedule = new ArrayList<SmallSchedule>();
        newSchedule = smallScheduleAdapter.getSmallSchedules();
        smallSchedules = null;
        smallSchedules = new ArrayList<SmallSchedule>();
        smallSchedules.addAll(newSchedule);
    }


    public void findPlace(View view) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, Utills.GOOGLE_AUTOCOMPLETE_REQUESTCODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    int index;

    public void setLoadTagIndex(int idx) {
        index = idx;
    }

    public int getLoadTagIndex() {
        return index;
    }

    public void loadTag(int idx) {
        RealmResults<TagListRealm> taglists = realm.where(TagListRealm.class).equalTo("tag_id", mTag.get(idx).getId()).findAll();
        smallSchedules = new ArrayList<>();
        for (int i = 0; i < taglists.size(); i++) {
            SmallSchedule tmp = new SmallSchedule();
            tmp.setSmall_time(taglists.get(i).getTag_date());
            tmp.setSmall_time_long(taglists.get(i).getTag_time_long());
            tmp.setSmall_tilte(taglists.get(i).getTag_list_name());
            tmp.setAlarm_flag(false);
            smallSchedules.add(tmp);
        }
        refreshRecyclerView(smallSchedules, departSchedule, mDates);
    }


    boolean[] mWeekDaysSetState = {false, false, false, false, false, false, false};

    @Override
    public void onClick(View v) {
        Log.d("요일체크", "요일");
        int unCheck = (1 << 8) - 1; // 1111 1111
        switch (v.getId()) {
            case R.id.add_weekday_sun:
                if (mWeekDaysSetState[0]) {
                    mWeekDaysSetState[0] = false;
                } else {
                    mWeekDaysSetState[0] = true;
                }
                getDataFromWeekView();
                setWeekDayToView(mWeekOfDays);
                break;

            case R.id.add_weekday_mon:
                Log.d("요일체크", "화요일");
                if (mWeekDaysSetState[1]) {
                    mWeekDaysSetState[1] = false;
                } else {
                    mWeekDaysSetState[1] = true;
                }
                getDataFromWeekView();
                setWeekDayToView(mWeekOfDays);
                break;

            case R.id.add_weekday_tue:
                if (mWeekDaysSetState[2]) {
                    mWeekDaysSetState[2] = false;
                } else {
                    mWeekDaysSetState[2] = true;
                }
                getDataFromWeekView();
                setWeekDayToView(mWeekOfDays);
                break;

            case R.id.add_weekday_wed:
                if (mWeekDaysSetState[3]) {
                    mWeekDaysSetState[3] = false;
                } else {
                    mWeekDaysSetState[3] = true;
                }
                getDataFromWeekView();
                setWeekDayToView(mWeekOfDays);
                break;

            case R.id.add_weekday_thu:
                if (mWeekDaysSetState[4]) {
                    mWeekDaysSetState[4] = false;
                } else {
                    mWeekDaysSetState[4] = true;
                }
                getDataFromWeekView();
                setWeekDayToView(mWeekOfDays);
                break;

            case R.id.add_weekday_fri:
                if (mWeekDaysSetState[5]) {
                    mWeekDaysSetState[5] = false;
                } else {
                    mWeekDaysSetState[5] = true;
                }
                getDataFromWeekView();
                setWeekDayToView(mWeekOfDays);
                break;

            case R.id.add_weekday_sat:
                if (mWeekDaysSetState[6]) {
                    mWeekDaysSetState[6] = false;
                } else {
                    mWeekDaysSetState[6] = true;
                }
                getDataFromWeekView();
                setWeekDayToView(mWeekOfDays);
                break;

            default:
                break;
        }
        Log.d("값체크", String.valueOf(mWeekOfDays));
    }


    public void setWeekCheckState() {
        for (int i = 1; i < 8; i++) {
            int flag = Utills.checkTargetWeekOfDayIsSet(mWeekOfDays, i);
            if (flag != 0) {
                mWeekDaysSetState[0] = true;
            }
        }
    }

    public void getDataFromWeekView() {

        int idx = 1;
        int unCheck = (1 << 8) - 1; // 1111 1111
        for (boolean isChecked : mWeekDaysSetState) {

            if(isChecked) {
                Log.d("체크여부", "참");
            } else{
                Log.d("체크여부", "거짓");
            }

            if (isChecked) {
                mWeekOfDays |= (1 << idx);
            } else{
                mWeekOfDays &= (unCheck ^ getExponential(2, idx));
            }
            idx++;
        }
    }

    public int getExponential(int bot, int top){
        int tmp=1;
        for(int i=1; i<=top; i++){
            tmp *= bot;
        }
        return tmp;
    }
}
