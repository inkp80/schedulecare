package com.inkp.boostcamp.Boostme.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.TagListAdapter;
import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.TagListObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.ButterKnife;
import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import io.realm.Realm;

/**
 * Created by inkp on 2017-02-20.
 */

public class AddTagListActivity extends AppCompatActivity {
    TagListAdapter mTagListAdapter;
    RecyclerView mTagListRecylcerView;
    List<TagListObject> mTagList;
    Realm realm;

    private InputMethodManager imm;
    int mTagId;
    String mTagTitle;
    Calendar calendar;
    Date date;


    @Override
    public void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_add_tag);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.tag_add_toolbar));

        calendar = new GregorianCalendar(); date = new Date();
        realm = Realm.getDefaultInstance();
        mTagId = Utills.getNextKeyTag(realm);
        mTagList = new ArrayList<>();

        mTagListAdapter = new TagListAdapter(mTagList);
        mTagListRecylcerView = (RecyclerView) findViewById(R.id.addtag_taglist_recyclerview);
        mTagListRecylcerView.hasFixedSize();
        mTagListRecylcerView.setLayoutManager(new LinearLayoutManager(this));
        mTagListRecylcerView.setAdapter(mTagListAdapter);


        ItemTouchHelper.Callback callback = new RVHItemTouchHelperCallback(mTagListAdapter, true, true, true);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mTagListRecylcerView);

        mTagListRecylcerView.addItemDecoration(
                new RVHItemDividerDecoration(this, LinearLayoutManager.VERTICAL));

        mTagListRecylcerView.addOnItemTouchListener(
                new RVHItemClickListener(this, new RVHItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //do notthing
                    }
                })
        );
    }




}
