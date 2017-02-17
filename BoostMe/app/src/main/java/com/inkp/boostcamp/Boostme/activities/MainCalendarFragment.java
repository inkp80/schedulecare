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

    public List<String> mStrData;

    public RecyclerView mScheduleRecyclerView;
    public RobotoCalendarView mRobotoCalendar;

    public RealmResults<ScheduleRealm> mSchedules;
    public RealmResults<ScheduleRealm> mSchedulesForShown;

    Calendar mToday;
    Calendar mTomorrow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        Date mTodayInDate = new Date();

        mToday = new GregorianCalendar();
        mToday.setTime(mTodayInDate);
        Log.d("check week of day", String.valueOf(mToday.get(Calendar.DAY_OF_WEEK)));
        mToday.set(Calendar.HOUR_OF_DAY, 0);
        mToday.set(Calendar.MINUTE, 0);
        mToday.set(Calendar.SECOND, 0);
        mToday.set(Calendar.MILLISECOND, 0);


        mTomorrow = new GregorianCalendar();
        mTomorrow.setTimeInMillis(mToday.getTimeInMillis()+24*60*60*1000);

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
        View v = inflater.inflate(R.layout.activity_main_calendar, container, false);

        mScheduleRecyclerView = (RecyclerView) v.findViewById(R.id.main_calendar_recycler_view);

        mRobotoCalendar = (RobotoCalendarView) v.findViewById(R.id.main_calendar);
        mRobotoCalendar.setRobotoCalendarListener((RobotoCalendarView.RobotoCalendarListener) this);
        mRobotoCalendar.setShortWeekDays(false);
        mRobotoCalendar.showDateTitle(true);
        mRobotoCalendar.updateView();


        makeCheckMarkOnDay();
        //mRobotoCalendar.markCircleImage1(mToday);


        mSchedules = realm.where(ScheduleRealm.class).greaterThan("date_in_long", mToday.getTimeInMillis()).lessThan("date_in_long", mTomorrow.getTimeInMillis()).findAll();

        ScheduleAdapter mScheduleAdapter = new ScheduleAdapter(getActivity(), mSchedules);
        mScheduleRecyclerView.hasFixedSize();
        mScheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mScheduleRecyclerView.setAdapter(mScheduleAdapter);

        return v;
    }

    @Override
    public void onDayClick(Calendar daySelectedCalendar) {
        mToday.setTimeInMillis(daySelectedCalendar.getTimeInMillis());
        mToday.set(Calendar.HOUR_OF_DAY, 0);
        mToday.set(Calendar.MINUTE, 0);
        mToday.set(Calendar.SECOND, 0);
        mToday.set(Calendar.MILLISECOND, 0);
        mTomorrow.setTimeInMillis(mToday.getTimeInMillis() + 24*60*60*1000);


        mSchedules = realm.where(ScheduleRealm.class).greaterThanOrEqualTo("date_in_long", mToday.getTimeInMillis()).lessThan("date_in_long", mTomorrow.getTimeInMillis()).findAll();

        Log.d("###size", String.valueOf(mSchedules.size()));

        ScheduleAdapter mScheduleAdapter = new ScheduleAdapter(getActivity(), mSchedules);
        mScheduleRecyclerView.setAdapter(mScheduleAdapter);
        Toast.makeText(getActivity(), "onDayClick: " + mToday.getTime() + "/" + mTomorrow.getTime(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDayLongClick(Calendar daySelectedCalendar) {
        Toast.makeText(getActivity(), "onDayLongClick: " + daySelectedCalendar.getTime(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRightButtonClick() {
        Toast.makeText(getActivity(), "onRightButtonClick!", Toast.LENGTH_SHORT).show();
        mToday.add(Calendar.MONTH, 1);
        makeCheckMarkOnDay();
    }

    @Override
    public void onLeftButtonClick() {
        Toast.makeText(getActivity(), "onLeftButtonClick!", Toast.LENGTH_SHORT).show();

        mToday.add(Calendar.MONTH, -1);
        makeCheckMarkOnDay();
    }

    void makeCheckMarkOnDay(){
        Calendar marking_start_date = new GregorianCalendar();
        marking_start_date.setTimeInMillis(mToday.getTimeInMillis());
        marking_start_date.set(Calendar.DATE, 1);

        Calendar marking_end_date = new GregorianCalendar();
        marking_end_date.setTimeInMillis(mToday.getTimeInMillis());
        marking_end_date.set(Calendar.DATE, marking_end_date.getActualMaximum(Calendar.DAY_OF_MONTH));
        Log.d("###fin date", String.valueOf(marking_end_date.getTime()));

        RealmResults<ScheduleRealm> mListOfMonth = realm.where(ScheduleRealm.class).greaterThanOrEqualTo("date_in_long", marking_start_date.getTimeInMillis()).lessThan("date_in_long", marking_end_date.getTimeInMillis()).findAll();
        Log.d("###size", String.valueOf(mListOfMonth.size()));
        Calendar marking = new GregorianCalendar();

        for(int i=0; i<mListOfMonth.size(); i++) {
            marking.setTimeInMillis(mListOfMonth.get(i).getDate_in_long());
            mRobotoCalendar.markCircleImage1(marking);
        }

    }

}
