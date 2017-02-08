package com.inkp.boostcamp.Boostme;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>
        implements RVHAdapter{
    private List<String> DataFromMain;

    public ScheduleAdapter(List<String> data){
        this.DataFromMain = data;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_view_holder, parent, false);
        //view.setOnClickListener(mOnClickListener);
        return new ScheduleViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        holder.scheduleViewHolder_TitleTextView.setText(DataFromMain.get(position));
    }

    @Override
    public int getItemCount() {
        return DataFromMain.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
        Log.d("Adapter", "item moved");
        return false;
    }

    @Override
    public void onItemDismiss(int position, int direction) {
        remove(position);

    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder implements RVHViewHolder{
        final TextView scheduleViewHolder_TitleTextView;
        //String scheduleViewHolder_title;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            scheduleViewHolder_TitleTextView = (TextView) itemView.findViewById(R.id.schedule_view_holder_TextView);
        }

        @Override
        public void onItemSelected(int actionstate) {
            Log.d("Item Selected","######");
        }

        @Override
        public void onItemClear() {

        }
    }

    private void remove(int position) {
        DataFromMain.remove(position);
        notifyItemRemoved(position);
    }

    private void swap(int firstPosition, int secondPosition) {
        Collections.swap(DataFromMain, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
    }

}
