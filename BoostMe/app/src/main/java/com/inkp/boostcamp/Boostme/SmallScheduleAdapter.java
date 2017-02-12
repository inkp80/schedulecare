package com.inkp.boostcamp.Boostme;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inkp.boostcamp.Boostme.data.SmallSchedule;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by macbook on 2017. 2. 10..
 */

public class SmallScheduleAdapter extends RecyclerView.Adapter<SmallScheduleAdapter.SmallScheduleViewHolder>
        implements RVHAdapter{

    Date MainDate;
    SimpleDateFormat format_small = new SimpleDateFormat("hh : mm a");
    // dateView.setText(sdf.format(Dates).toString());
    public ArrayList<SmallSchedule> smallSchedules;

    public SmallScheduleAdapter(ArrayList<SmallSchedule> s_schedules, Date parentDate){
        MainDate = parentDate;
        smallSchedules = s_schedules;
    }

    /*@Override
    public SmallScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.small_schedule_view_holder, parent, false);
        return new SmallScheduleViewHolder(view);
    }*/

    @Override
    public SmallScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.small_schedule_view_holder, parent, false);
        return new SmallScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SmallScheduleAdapter.SmallScheduleViewHolder holder, int position) {

        //String TimeToShow = format_small.format(smallSchedules.get(position).getSmall_time());
        int hour = smallSchedules.get(position).getSmall_time().getHours();
        int minute = smallSchedules.get(position).getSmall_time().getMinutes();
        MainDate.setHours(MainDate.getHours() - hour);
        MainDate.setMinutes(MainDate.getMinutes() - minute);
        minute = minute + hour * 60;

        holder.smallTitleView.setText(smallSchedules.get(position).getSmall_tilte());
        holder.smallTimeView.setText(String.valueOf(minute));

    }

    @Override
    public int getItemCount() {
        return smallSchedules.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition){
        swap(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position, int direction) {
        remove(position);
    }


    public void dataChagned(ArrayList<SmallSchedule> newSmallSchedule){
        smallSchedules = newSmallSchedule;
        //notifyDataSetChanged();
    }


    class SmallScheduleViewHolder extends RecyclerView.ViewHolder implements RVHViewHolder {
        final TextView smallTitleView;
        final TextView smallTimeView;
        final int orderOfTask = -1;
        final boolean isAlarm = false;

        public SmallScheduleViewHolder(View itemView) {
            super(itemView);
            smallTitleView = (TextView) itemView.findViewById(R.id.small_schedule_title);
            smallTimeView = (TextView) itemView.findViewById(R.id.small_schedule_time);
        }

        @Override
        public void onItemSelected(int actionstate) {
            Log.d("Item Selected", "######");
        }

        @Override
        public void onItemClear() {
        }
    }



    private void remove(int position) {
        smallSchedules.remove(position);
        notifyItemRemoved(position);
    }

    private void swap(int firstPosition, int secondPosition) {
        Collections.swap(smallSchedules, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }



}

