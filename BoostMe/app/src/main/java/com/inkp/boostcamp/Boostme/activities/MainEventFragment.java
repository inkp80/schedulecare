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
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by macbook on 2017. 2. 8..
 */

public class MainEventFragment extends Fragment {

    Realm realm;
    private ScheduleAdapter Event_scheduleAdapter;
    public RecyclerView Event_scheduleRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
        RealmResults<ScheduleRealm> Schedule = realm.where(ScheduleRealm.class).findAll();
        Event_scheduleAdapter = new ScheduleAdapter(getActivity(), Schedule);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_event, container, false);
        Event_scheduleRecyclerView = (RecyclerView) v.findViewById(R.id.main_event_recycler_view);

        Event_scheduleRecyclerView.hasFixedSize();
        Event_scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Event_scheduleRecyclerView.setAdapter(Event_scheduleAdapter);
        return v;
    }
}
