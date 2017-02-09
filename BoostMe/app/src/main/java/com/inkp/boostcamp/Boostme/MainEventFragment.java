package com.inkp.boostcamp.Boostme;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marcohc.robotocalendar.RobotoCalendarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macbook on 2017. 2. 8..
 */

public class MainEventFragment extends Fragment {

    public List<String> StrData2;
    private ScheduleAdapter Event_scheduleAdapter;
    public RecyclerView Event_scheduleRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_event, container, false);

        Event_scheduleRecyclerView = (RecyclerView) v.findViewById(R.id.main_event_recycler_view);

        StrData2 = new ArrayList<>();
        StrData2.add("a");
        StrData2.add("b");
        StrData2.add("c");
        StrData2.add("e");


        Event_scheduleAdapter = new ScheduleAdapter(StrData2);
        Event_scheduleRecyclerView.hasFixedSize();
        Event_scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Event_scheduleRecyclerView.setAdapter(Event_scheduleAdapter);

        return v;
    }
}
