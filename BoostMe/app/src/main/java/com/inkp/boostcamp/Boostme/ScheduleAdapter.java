package com.inkp.boostcamp.Boostme;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.activities.DetailActivity;
import com.inkp.boostcamp.Boostme.activities.MainActivity;
import com.inkp.boostcamp.Boostme.data.ScheduleParcel;
import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;


public class ScheduleAdapter extends RealmRecyclerViewAdapter<ScheduleRealm, ScheduleAdapter.ScheduleViewHolder> {

    Realm realm;
    Context mContext;
    RealmResults<ScheduleRealm> mScheduleData;
    RealmResults<SmallScheduleRealm> mSmallSchedules;

    public ScheduleAdapter(Context context, RealmResults<ScheduleRealm> results) {
        super(context, results, true);
        realm = Realm.getDefaultInstance();
        mScheduleData = results;
        mContext = context;
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

        mSmallSchedules = realm.where(SmallScheduleRealm.class).equalTo("schedule_id", mScheduleData.get(position).getId()).findAll();

        boolean mIsSmallExisit = false;
        for(int i=0; i<mSmallSchedules.size(); i++){
            if(mSmallSchedules.get(i).isAlarm_flag()){
                holder.mViewHolder_alarmButton.setChecked(true);
                mIsSmallExisit = true;
                break;
            }
        } if(mIsSmallExisit== false){
            holder.mViewHolder_alarmButton.setChecked(false);
        }

        if (holder.data.getWeek_of_day_repit() != 0) {
            holder.ViewHolder_date.setText(Utills.format_a_hhmm.format(holder.data.getDate()));
            holder.ViewHolder_date.append(", ");
            setWeekdayOnView(holder.data.getWeek_of_day_repit(), holder.ViewHolder_date);
        } else
            holder.ViewHolder_date.setText(Utills.format_yymmdd_hhmm_a.format(holder.data.getDate()));

        holder.ViewHolder_title.setText(holder.data.getTitle());
        holder.holder_schedule_id = obj.getId();
    }

    public void dataChaged(RealmResults<ScheduleRealm> new_result) {
        mScheduleData = new_result;
        notifyDataSetChanged();
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ViewHolder_title;
        public TextView ViewHolder_date;
        public SwitchCompat mViewHolder_alarmButton;
        public ScheduleRealm data;

        int holder_schedule_id;

        public ScheduleViewHolder(View view) {
            super(view);

            ViewHolder_title = (TextView) view.findViewById(R.id.schedule_view_holder_Title);
            ViewHolder_date = (TextView) view.findViewById(R.id.schedule_view_holder_date);
            mViewHolder_alarmButton = (SwitchCompat) view.findViewById(R.id.schedule_view_holder_alarmbutton);

            mViewHolder_alarmButton.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            if (mViewHolder_alarmButton.isChecked()) {
                                //mViewHolder_alarmButton.setChecked(false);
                                Utills.cancelAlarmByMainButton(mContext, holder_schedule_id);
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm bgRealm) {
                                        //mSmallSchedule.get(idx).setAlarm_flag(false);
                                        ScheduleRealm schedule = bgRealm.where(ScheduleRealm.class).equalTo("id", holder_schedule_id).findFirst();
                                        schedule.setAlarm_flag(false);
                                        bgRealm.insertOrUpdate(schedule);
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("REALM", "main Data Sucess");
                                    }
                                });
                            } else {
                                //mViewHolder_alarmButton.setChecked(true);
                                Utills.setAlarmByMainButton(mContext, holder_schedule_id, data);
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm bgRealm) {
                                        //mSmallSchedule.get(idx).setAlarm_flag(false);
                                        ScheduleRealm schedule = bgRealm.where(ScheduleRealm.class).equalTo("id", holder_schedule_id).findFirst();
                                        schedule.setAlarm_flag(true);
                                        bgRealm.insertOrUpdate(schedule);
                                    }
                                }, new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        Log.d("REALM", "main Data Sucess1");
                                    }
                                });
                            }
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });

            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Log.d("check in", "inhere");
            switch (v.getId()) {
                default:
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra(Utills.access_Schedule_id, holder_schedule_id);
                    mContext.startActivity(intent);
                    break;
            }

        }
    }

    public void setWeekdayOnView(int val, TextView tv) {
        for (int i = 1; i < 8; i++) {
            int flag = Utills.checkTargetWeekOfDayIsSet(val, i);
            if (flag != 0) {
                switch (i) {
                    case 1:
                        tv.append("일 ");
                        break;
                    case 2:
                        tv.append("월 ");
                        break;
                    case 3:
                        tv.append("화 ");
                        break;
                    case 4:
                        tv.append("수 ");
                        break;
                    case 5:
                        tv.append("목 ");
                        break;
                    case 6:
                        tv.append("금 ");
                        break;
                    case 7:
                        tv.append("토 ");
                        break;
                }
            }


        }
    }


}
