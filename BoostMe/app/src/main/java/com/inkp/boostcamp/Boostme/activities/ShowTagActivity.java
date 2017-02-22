package com.inkp.boostcamp.Boostme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.TagAdapter;
import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.TagRealm;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by inkp on 2017-02-20.
 */

public class ShowTagActivity extends AppCompatActivity {

    Realm realm;
    RealmResults<TagRealm> mTagResult;
    RecyclerView mRecyclerView;
    TagAdapter mTagAdapter;

    @BindView(R.id.toolbar_tag_add_button)
    ImageButton mTagAddButton;

    @Override
    public void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_show_tag);
        realm = Realm.getDefaultInstance();

        mTagResult = realm.where(TagRealm.class).findAll();

        mRecyclerView = (RecyclerView) findViewById(R.id.show_recyclerview);

        mTagAdapter = new TagAdapter(getBaseContext(), mTagResult);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mTagAdapter);

        ButterKnife.bind(this);
        mTagAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowTagActivity.this, AddTagListActivity.class);
//                /**/intent.putExtra()
                startActivity(intent);
            }
        });
    }
}