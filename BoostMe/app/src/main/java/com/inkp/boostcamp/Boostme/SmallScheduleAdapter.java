package com.inkp.boostcamp.Boostme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.activities.AddTaskActivity;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    long main_date_long;
    SmallSchedule departSchedule;
    SmallSchedule finalSchedule;
    List<SmallSchedule> smallSchedules;


    public List<SmallSchedule> getSmallSchedules(){
        return smallSchedules;
    }

    public SmallScheduleAdapter(List<SmallSchedule> s_schedules, Date parentDate, long main_time_long, SmallSchedule depart, SmallSchedule fin){
        MainDate = parentDate;
        main_date_long = main_time_long;
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(smallSchedules.get(position).getSmall_time_long());
        long hour = calendar.get(Calendar.HOUR_OF_DAY);
        long minute = calendar.get(Calendar.MINUTE);

        calendar.setTimeInMillis(smallSchedules.get(position).getAlert_time());
        Date date = new Date(calendar.getTimeInMillis());

        holder.smallTitleView.setText(smallSchedules.get(position).getSmall_tilte() );

        holder.smallTimeView.setText(Utills.format_hhmm_a.format(date) +"(" +String.valueOf(hour*60 + minute)+"분)");

        holder.pos = position;
        holder.isAlarm = smallSchedules.get(position).isAlarm_flag();
        if(holder.isAlarm){
            holder.smallAlarmButton.setImageResource(R.drawable.alarm_orange);
        }else
            holder.smallAlarmButton.setImageResource(R.drawable.alarm_white);
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


    public void dataChagned(List<SmallSchedule> newSmallSchedule, Date newDate){
        smallSchedules = newSmallSchedule;
        MainDate = newDate;
        main_date_long = MainDate.getTime();
        timeLineSetting();
        notifyDataSetChanged();
    }


    class SmallScheduleViewHolder extends RecyclerView.ViewHolder implements RVHViewHolder{
        final TextView smallTitleView;
        final TextView smallTimeView;
        final ImageButton smallAlarmButton;
        int pos=0;
        boolean isAlarm = false;

        public SmallScheduleViewHolder(View itemView) {
            super(itemView);
            smallTitleView = (TextView) itemView.findViewById(R.id.small_schedule_title);
            smallTimeView = (TextView) itemView.findViewById(R.id.small_schedule_time);
            smallAlarmButton = (ImageButton) itemView.findViewById(R.id.small_schedule_alarm);
        }


        @Override
        public void onItemSelected(int actionstate) {
            if(isAlarm){
                smallAlarmButton.setImageResource(R.drawable.alarm_white);
                isAlarm=false;
                smallSchedules.get(pos).setAlarm_flag(false);
            }
            else{
                smallAlarmButton.setImageResource(R.drawable.alarm_orange);
                isAlarm=true;
                smallSchedules.get(pos).setAlarm_flag(true);
            }
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

        Calendar line_calendar = Calendar.getInstance();
        line_calendar.setTimeInMillis(main_date_long);
        Log.d("###Main", String.valueOf(line_calendar.getTime()));


        Calendar calLine = Calendar.getInstance();
        for(int i=smallSchedules.size()-1; i>=0; i--){
            calLine.setTimeInMillis(smallSchedules.get(i).getSmall_time_long());

            smallSchedules.get(i).setEnd_time(line_calendar.getTimeInMillis());

            line_calendar.add(Calendar.HOUR_OF_DAY, -calLine.get(Calendar.HOUR_OF_DAY));
            line_calendar.add(Calendar.MINUTE, -calLine.get(Calendar.MINUTE));

            smallSchedules.get(i).setAlert_time(line_calendar.getTimeInMillis());
        }
    }

}

