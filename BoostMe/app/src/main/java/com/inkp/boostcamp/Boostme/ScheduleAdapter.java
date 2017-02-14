package com.inkp.boostcamp.Boostme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.data.ScheduleRealm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;


public class ScheduleAdapter extends RealmRecyclerViewAdapter<ScheduleRealm, ScheduleAdapter.ScheduleViewHolder>{

    public ScheduleAdapter(Context context, RealmResults<ScheduleRealm> results){
        super(context, results, true);
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_view_holder, parent, false);
        return new ScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        ScheduleRealm obj = getData().get(position);
        holder.data = obj;
        holder.ViewHolder_title.setText(holder.data.getTitle());
        holder.ViewHolder_date.setText(Utills.format_yymmdd_hhmm_a.format(holder.data.getDate()));

    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView ViewHolder_title;
        public TextView ViewHolder_date;
        public SwitchCompat ViewHolder_alarmButton;
        public ScheduleRealm data;
        public ScheduleViewHolder(View view){
            super(view);
            ViewHolder_title = (TextView) view.findViewById(R.id.schedule_view_holder_Title);
            ViewHolder_date = (TextView) view.findViewById(R.id.schedule_view_holder_date);
            ViewHolder_alarmButton = (SwitchCompat) view.findViewById(R.id.schedule_view_holder_alarmbutton);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.schedule_view_holder_alarmbutton :
                    //alarm on/off
                    if(ViewHolder_alarmButton.isChecked())
                        ViewHolder_alarmButton.setChecked(false);
                    else
                        ViewHolder_alarmButton.setChecked(true);
                    break;
                    default:
                        Toast.makeText(context, "To Detail View", Toast.LENGTH_SHORT).show();
                        break;
            }

        }
    }


}
