package com.inkp.boostcamp.Boostme;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;
import com.inkp.boostcamp.Boostme.receiver.AlarmReceiver;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by inkp on 2017-02-16.
 */

public class DetailScheduleAdapter extends RealmRecyclerViewAdapter<SmallScheduleRealm, DetailScheduleAdapter.DetailScheduleViewHolder> {

    Realm realm;
    Context mContext;
    RealmResults<SmallScheduleRealm> mSmallList;
    ScheduleRealm mMainSchedule;


    public DetailScheduleAdapter(Context context, RealmResults<SmallScheduleRealm> results, ScheduleRealm main_schedule) {
        super(context, results, true);
        realm = Realm.getDefaultInstance();
        mMainSchedule = main_schedule;
        mSmallList = results;
        mContext = context;
    }

    @Override
    public DetailScheduleAdapter.DetailScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_schedule_view_holder, parent, false);
        return new DetailScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DetailScheduleViewHolder holder, int position) {
        SmallScheduleRealm obj = getData().get(position);
        holder.data = obj;
        holder.orderval = obj.getOrder_value();
        Date viewDate = new Date(holder.data.getAlarm_start_time());
        holder.holder_title.setText(holder.data.getSmall_tilte());
        holder.holder_date.setText(Utills.format_hhmm_a.format(viewDate));
        holder.alarm_on_off.setChecked(holder.data.isAlarm_flag());
    }


    List<Integer> mListToClear;
    int skip_idx;

    class DetailScheduleViewHolder extends RecyclerView.ViewHolder {
        public TextView holder_title;
        public TextView holder_date;
        public SmallScheduleRealm data;
        public SwitchCompat alarm_on_off;
        public int orderval;

        public DetailScheduleViewHolder(View view) {
            super(view);
            alarm_on_off = (SwitchCompat) view.findViewById(R.id.detail_viewholder_alarm_button);
            holder_title = (TextView) view.findViewById(R.id.detail_viewholder_title);
            holder_date = (TextView) view.findViewById(R.id.detail_viewholder_date);

            alarm_on_off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = orderval;
                    final boolean flag = alarm_on_off.isChecked();
                    Log.d("is true", String.valueOf(flag));
                    final int main_id = mMainSchedule.getId();
                    Utills.cancleAlarm(mContext, mMainSchedule.getId(), mSmallList);
                    enrollAlarm(pos, flag);

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            SmallScheduleRealm schedule_object = bgRealm.where(SmallScheduleRealm.class).equalTo("schedule_id", main_id).equalTo("order_value", pos).findFirst();
                            schedule_object.setAlarm_flag(flag);
                            bgRealm.insertOrUpdate(schedule_object);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Log.d("REALM", "small main Data Sucess");
                        }
                    });
                }
            });
        }
    }

    public void enrollAlarm(int pos, boolean switch_flag){
        Calendar calendar = Calendar.getInstance();
        Calendar trigger_time = Calendar.getInstance();
        for(int i=0; i<mSmallList.size(); i++){
            if(!switch_flag && pos==i) continue;

            if(mSmallList.get(i).isAlarm_flag() || (switch_flag && pos == i)) {
                trigger_time.setTimeInMillis(mSmallList.get(i).getAlarm_start_time());
                if (calendar.getTimeInMillis() > trigger_time.getTimeInMillis()) continue;
                setAlarm(trigger_time.getTimeInMillis(), i);
            }
        }
    }

    public void setAlarm(long trigger_time_long, int index){
        Intent intent = new Intent(mContext, AlarmReceiver.class);

        intent.putExtra(Utills.ALARM_intent_scheduleId, mMainSchedule.getId()); //메인아이디
        intent.putExtra(Utills.ALARM_intent_title, mMainSchedule.getTitle()); //메인 타이틀
        intent.putExtra(Utills.ALARM_intent_date, mMainSchedule.getDate_in_long()); //메인 시간
        intent.putExtra(Utills.ALARM_intent_scheduleIdx, index); //세부 인덱스
        intent.putExtra(Utills.ALARM_intent_weekofday, mMainSchedule.getWeek_of_day_repit()); //요일 반복
        intent.putExtra(Utills.ALARM_intent_small_title, mSmallList.get(index).getSmall_tilte());

        int alarm_id = Utills.alarmIdBuilder(mMainSchedule.getId(), index);
        PendingIntent pendingIntent
                = PendingIntent.getBroadcast(mContext, alarm_id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Utills.enrollAlarm(mContext, pendingIntent, Utills.setTriggerTime(trigger_time_long));

    }

}
