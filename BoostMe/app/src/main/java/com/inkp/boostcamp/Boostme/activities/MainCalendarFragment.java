package com.inkp.boostcamp.Boostme.activities;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.transition.Scene;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.RobotoCalendarView;
import com.inkp.boostcamp.Boostme.ScheduleAdapter;
import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;

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
import io.realm.Sort;

        /*
        Calendar calendar = Calendar.getInstance();
        Random random = new Random(System.currentTimeMillis());
        int style = random.nextInt(2);
        int daySelected = random.nextInt(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, daySelected);

        Marking on Date
        robotoCalendarView.markCircleImage1(calendar);
        */

public class MainCalendarFragment extends Fragment implements RobotoCalendarView.RobotoCalendarListener {
    Realm realm;

    public List<String> StrData;
    private ScheduleAdapter scheduleAdapter;

    //@BindView(R.id.main_schedule_recycler_view)
    public RecyclerView scheduleRecyclerView;
    //@BindView(R.id.main_calendar)
    public RobotoCalendarView robotoCalendar;

    public RealmResults<ScheduleRealm> Schedules;
    public RealmResults<ScheduleRealm> schedulesForShown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        Date todayDate = new Date();
        todayDate.setHours(0);
        todayDate.setMinutes(0);
        todayDate.setSeconds(0);
        Log.d("###today", Utills.format_yymmdd_hhmm_a.format(todayDate));

        Date tomorrow = new Date();
        tomorrow.setTime(todayDate.getTime()+24*60*60*1000);
        //tomorrow.setHours(24);
        //tomorrow.setMinutes(0);
        //tomorrow.setSeconds(0);

        Log.d("###tomo", Utills.format_yymmdd_hhmm_a.format(tomorrow));


//.lessThan("date", tomorrow).
        Schedules = realm.where(ScheduleRealm.class).lessThan("date", tomorrow).findAll();

        //Schedules = realm.where(ScheduleRealm.class).findAll();
        Schedules = Schedules.sort("date", Sort.ASCENDING);

        Log.d("####size", String.valueOf(Schedules.size()));
        scheduleAdapter = new ScheduleAdapter(getActivity(), Schedules);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //ButterKnife.bind(this);
        View v = inflater.inflate(R.layout.activity_main_calendar, container, false);

        scheduleRecyclerView = (RecyclerView) v.findViewById(R.id.main_calendar_recycler_view);
        robotoCalendar = (RobotoCalendarView) v.findViewById(R.id.main_calendar);

        robotoCalendar.setRobotoCalendarListener((RobotoCalendarView.RobotoCalendarListener) this);
        robotoCalendar.setShortWeekDays(false);
        robotoCalendar.showDateTitle(true);
        robotoCalendar.updateView();

        scheduleRecyclerView.hasFixedSize();
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        scheduleRecyclerView.setAdapter(scheduleAdapter);



        //Calendar calendar = new GregorianCalendar();
        //calendar.setTime(Schedules.get(0).getDate());
        //robotoCalendar.markCircleImage1(calendar);

        return v;
    }

    @Override
    public void onDayClick(Calendar daySelectedCalendar) {
        Toast.makeText(getActivity(), "onDayClick: " + daySelectedCalendar.getTime(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDayLongClick(Calendar daySelectedCalendar) {
        Toast.makeText(getActivity(), "onDayLongClick: " + daySelectedCalendar.getTime(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRightButtonClick() {
        Toast.makeText(getActivity(), "onRightButtonClick!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLeftButtonClick() {
        Toast.makeText(getActivity(), "onLeftButtonClick!", Toast.LENGTH_SHORT).show();
    }

    public void getDataFromSelectedDate(Date date){
        for(int i=0; i<Schedules.size(); i++) {
            Date CurPosDate = Schedules.get(i).getDate();
            CurPosDate.setHours(0);
            CurPosDate.setMinutes(0);
            if(CurPosDate == date)
                schedulesForShown.add(Schedules.get(i));
        }
    }
}
