package com.inkp.boostcamp.Boostme;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inkp.boostcamp.Boostme.data.TagListObject;
import com.inkp.boostcamp.Boostme.data.TagListRealm;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import github.nisrulz.recyclerviewhelper.RVHAdapter;
import github.nisrulz.recyclerviewhelper.RVHViewHolder;

/**
 * Created by inkp on 2017-02-20.
 */

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.TagListViewHolder> implements RVHAdapter{

    Date MainDate;
    List<TagListObject> mTagList;

    public List<TagListObject> getmTagList(){
        return mTagList;
    }

    public TagListAdapter(List<TagListObject> lists){
        Log.d("####", " 생성자");
        mTagList =lists;
        Log.d("###", String.valueOf(mTagList.size()));
    }


    @Override
    public TagListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("####,", "create viewholder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.taglist_view_holder, parent, false);
        return new TagListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagListAdapter.TagListViewHolder holder, int position) {
        holder.data = mTagList.get(position);
        holder.mTagListName.setText(mTagList.get(position).getTag_list_name());
        holder.mTagListTime.setText(Utills.format_a_hhmm.format(mTagList.get(position).getTag_date()));


        Log.d("###", "in Bind");
        Log.d("###", String.valueOf(mTagList.get(position).getTag_list_name()));
    }

    @Override
    public int getItemCount() {
        return mTagList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        swap(fromPosition, toPosition);
        return false;
    }

    @Override
    public void onItemDismiss(int position, int direction) {
        remove(position);
    }

    class TagListViewHolder extends RecyclerView.ViewHolder implements RVHViewHolder{
        TextView mTagListName;
        TextView mTagListTime;
        TagListObject data;
        Calendar time;

        public TagListViewHolder(View itemView) {
            super(itemView);
            mTagListName = (TextView) itemView.findViewById(R.id.taglist_name);
            mTagListTime = (TextView) itemView.findViewById(R.id.taglist_time);
        }

        @Override
        public void onItemSelected(int actionstate) {
        }

        @Override
        public void onItemClear() {

        }
    }

    private void remove(int position) {
        mTagList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    private void swap(int firstPosition, int secondPosition) {
        Collections.swap(mTagList, firstPosition, secondPosition);
        notifyItemMoved(firstPosition, secondPosition);
        notifyItemChanged(firstPosition);
        notifyItemChanged(secondPosition);
    }

    public void dataChagned(List<TagListObject> new_tag_list){
        mTagList = new_tag_list;
        notifyDataSetChanged();
    }



}
