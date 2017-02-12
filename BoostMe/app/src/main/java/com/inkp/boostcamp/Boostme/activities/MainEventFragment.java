package com.inkp.boostcamp.Boostme.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.ScheduleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by macbook on 2017. 2. 8..
 */

public class MainEventFragment extends Fragment {

    private ScheduleAdapter Event_scheduleAdapter;
    public RecyclerView Event_scheduleRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_event, container, false);

        Event_scheduleRecyclerView = (RecyclerView) v.findViewById(R.id.main_event_recycler_view);

        return v;
    }
}
