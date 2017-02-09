package com.inkp.boostcamp.Boostme;

import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;

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

    public List<String> StrData;
    private ScheduleAdapter scheduleAdapter;

    //@BindView(R.id.main_schedule_recycler_view)
    public RecyclerView scheduleRecyclerView;
    //@BindView(R.id.main_calendar)
    public RobotoCalendarView robotoCalendar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //ButterKnife.bind(this);

        View v = inflater.inflate(R.layout.activity_main_calendar, container, false);



        scheduleRecyclerView = (RecyclerView) v.findViewById(R.id.main_calendar_recycler_view);
        robotoCalendar = (RobotoCalendarView) v.findViewById(R.id.main_calendar);

        robotoCalendar.setRobotoCalendarListener((RobotoCalendarView.RobotoCalendarListener) this);
        robotoCalendar.setShortWeekDays(false);
        robotoCalendar.showDateTitle(true);
        robotoCalendar.updateView();
        StrData = new ArrayList<>();
        StrData.add("1");
        StrData.add("2");
        StrData.add("3");
        StrData.add("4");

        scheduleAdapter = new ScheduleAdapter(StrData);
        scheduleRecyclerView.hasFixedSize();
        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        scheduleRecyclerView.setAdapter(scheduleAdapter);

        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(scheduleAdapter, true, true, true);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(scheduleRecyclerView);

        scheduleRecyclerView.addItemDecoration(
                new RVHItemDividerDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        scheduleRecyclerView.addOnItemTouchListener(
                new RVHItemClickListener(getActivity(), new RVHItemClickListener.OnItemClickListener(){
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(), "Item Clicked", Toast.LENGTH_SHORT).show();
                    }
                })
        );


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
}
