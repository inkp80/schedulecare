package com.inkp.boostcamp.Boostme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.activities.AddTagListActivity;
import com.inkp.boostcamp.Boostme.data.TagRealm;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

/**
 * Created by inkp on 2017-02-21.
 */

public class TagAdapter extends RealmRecyclerViewAdapter<TagRealm, TagAdapter.TagViewHolder> {
    Context mContext;
    RealmResults<TagRealm> mTags;

    public TagAdapter(Context context, RealmResults<TagRealm> tags){
        super(context, tags, true);
        mContext = context;
        mTags = tags;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_view_holder, parent, false);
        return new TagViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        holder.data = mTags.get(position);
        holder.mTagTitle.setText(holder.data.getTag_name());
        holder.mTagId = holder.data.getTag_id();
    }


    class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        int mTagId;
        TagRealm data;
        TextView mTagTitle;
        public TagViewHolder(View view){
            super(view);
            mTagTitle = (TextView) view.findViewById(R.id.tag_title);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.tag_title){
                //data to Add TagList
                //Intent intent = new Intent(mContext, AddTagListActivity.class);
                //intent.putExtra(Utills.access_Schedule_id, holder_schedule_id);
                //mContext.startActivity(intent);
                return;
            }

        }
    }
}
