package com.inkp.boostcamp.Boostme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by inkp on 2017-02-16.
 */

public class DetailScheduleAdapter extends RealmRecyclerViewAdapter<SmallScheduleRealm, DetailScheduleAdapter.DetailScheduleViewHolder> {

    Context mContext;

    public DetailScheduleAdapter(Context context, RealmResults<SmallScheduleRealm> results){
        super(context, results, true);
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
        Date viewDate = new Date(holder.data.getAlarm_start_time());
        holder.holder_title.setText(holder.data.getSmall_tilte());
        holder.holder_date.setText(Utills.format_hhmm_a.format(viewDate));
    }


    class DetailScheduleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView holder_title;
        public TextView holder_date;
        public SmallScheduleRealm data;
        public SwitchCompat alarm_on_off;
        public DetailScheduleViewHolder(View view){
            super(view);
            alarm_on_off = (SwitchCompat) view.findViewById(R.id.detail_viewholder_alarm_button);
            holder_title = (TextView) view.findViewById(R.id.detail_viewholder_title);
            holder_date = (TextView) view.findViewById(R.id.detail_viewholder_date);
        }
        @Override
        public void onClick(View v) {

        }
    }

}
