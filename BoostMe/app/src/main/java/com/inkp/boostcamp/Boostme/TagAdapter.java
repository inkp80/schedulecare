package com.inkp.boostcamp.Boostme;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        holder.mTagId = holder.data.getId();
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }


    class TagViewHolder extends RecyclerView.ViewHolder{
        int mTagId;
        TagRealm data;
        TextView mTagTitle;
        public TagViewHolder(View view){
            super(view);
            mTagTitle = (TextView) view.findViewById(R.id.tag_title);
            mTagTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AddTagListActivity.class);
                    intent.putExtra(Utills.TAG_intent_tagid, data.getId());
                    intent.putExtra(Utills.Tag_intent_title, mTagTitle.getText().toString());
                    intent.putExtra(Utills.Tag_intent_action, 1);
                    mContext.startActivity(intent);
                }
            });
        }

    }
}
