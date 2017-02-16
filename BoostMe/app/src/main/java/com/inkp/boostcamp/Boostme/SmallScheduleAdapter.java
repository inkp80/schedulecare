package com.inkp.boostcamp.Boostme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.activities.AddTaskActivity;
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
    static final long ONE_MINUTE_IN_MILLIS=60000;//millisecs
    Date MainDate;
    SmallSchedule departSchedule;
    SmallSchedule finalSchedule;
    public ArrayList<SmallSchedule> smallSchedules;


    public ArrayList<SmallSchedule> getSmallSchedules(){
        return smallSchedules;
    }

    public SmallScheduleAdapter(ArrayList<SmallSchedule> s_schedules, Date parentDate, SmallSchedule depart, SmallSchedule fin){
        MainDate = parentDate;
        smallSchedules = s_schedules;
        timeLineSetting();
        departSchedule = depart;
        finalSchedule = fin;
    }

    @Override
    public SmallScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.small_schedule_view_holder, parent, false);
        return new SmallScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SmallScheduleAdapter.SmallScheduleViewHolder holder, int position) {

        //String TimeToShow = format_small.format(smallSchedules.get(position).getSmall_time());
        long hour = smallSchedules.get(position).getSmall_time().getHours();
        long minute = smallSchedules.get(position).getSmall_time().getMinutes();
        minute = minute + hour * 60;

        holder.smallTitleView.setText(smallSchedules.get(position).getSmall_tilte() );

        Date viewTime=new Date();
        viewTime.setTime(smallSchedules.get(position).getAlert_time());
        holder.smallTimeView.setText(Utills.format_hhmm_a.format(viewTime) +"(" +String.valueOf(minute)+"분)");
    }

    @Override
    public int getItemCount() {
        return smallSchedules.size();
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition){
        if(smallSchedules.get(fromPosition).isDepart_time()
                || smallSchedules.get(toPosition).isDepart_time()) return false;

        swap(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position, int direction) {
        remove(position);
    }

    public SmallSchedule printOnclick(int position){
        return smallSchedules.get(position);
    }


    public void dataChagned(ArrayList<SmallSchedule> newSmallSchedule, Date newDate){
        smallSchedules = newSmallSchedule;
        MainDate = newDate;
        timeLineSetting();
        notifyDataSetChanged();
        Log.d("#####", Utills.format_yymmdd_hhmm_a.format(MainDate));
    }


    class SmallScheduleViewHolder extends RecyclerView.ViewHolder implements RVHViewHolder{
        final TextView smallTitleView;
        final TextView smallTimeView;
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
        timeLineSetting();
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    private void swap(int firstPosition, int secondPosition) {
        Collections.swap(smallSchedules, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
        timeLineSetting();
        notifyItemChanged(firstPosition);
        notifyItemChanged(secondPosition);
    }

    public void timeLineSetting(){
        Date tmpDate = new Date(MainDate.getTime());
        long timeline = tmpDate.getTime();

        for(int i=smallSchedules.size()-1; i>=0; i--){

            Date time = smallSchedules.get(i).getSmall_time();
            long time_to_minute = time.getHours() * 60 * ONE_MINUTE_IN_MILLIS + time.getMinutes() * ONE_MINUTE_IN_MILLIS;


            smallSchedules.get(i).setEnd_time(timeline);
            smallSchedules.get(i).setAlert_time(timeline-time_to_minute);

            timeline -= time_to_minute;
            tmpDate.setTime(timeline);

        }
    }

}

