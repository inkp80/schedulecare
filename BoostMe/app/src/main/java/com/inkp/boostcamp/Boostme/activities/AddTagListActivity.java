package com.inkp.boostcamp.Boostme.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.inkp.boostcamp.Boostme.R;
import com.inkp.boostcamp.Boostme.TagListAdapter;
import com.inkp.boostcamp.Boostme.Utills;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.TagListObject;
import com.inkp.boostcamp.Boostme.data.TagListRealm;
import com.inkp.boostcamp.Boostme.data.TagRealm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import github.nisrulz.recyclerviewhelper.RVHItemClickListener;
import github.nisrulz.recyclerviewhelper.RVHItemDividerDecoration;
import github.nisrulz.recyclerviewhelper.RVHItemTouchHelperCallback;
import io.realm.Realm;

/**
 * Created by inkp on 2017-02-20.
 */

public class AddTagListActivity extends AppCompatActivity {
    public TagListAdapter mTagListAdapter;
    RecyclerView mTagListRecylcerView;
    List<TagListObject> mTagList;
    Realm realm;

    private InputMethodManager imm;
    int mTagId;
    String mTagTitle;
    Calendar calendar;
    Date date;

    @BindView(R.id.addtag_tag_title)
    EditText mTagTitleView;
    @BindView(R.id.addtag_taglist_additem)
    ImageButton mAddTagItemButton;

    @BindView(R.id.toolbar_addtag_save)
    ImageButton mAddTagButton;

    @Override
    public void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_add_tag);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.tag_add_toolbar));


        mTagListRecylcerView = (RecyclerView) findViewById(R.id.addtag_taglist_recyclerview);

        calendar = new GregorianCalendar(); date = new Date();
        realm = Realm.getDefaultInstance();
        mTagId = Utills.getNextKeyTag(realm);
        mTagList = new ArrayList<>();
        addTagListItem("title", new Date());
        Log.d("333333", mTagList.get(0).getTag_list_name());

        mTagListAdapter = new TagListAdapter(mTagList);
        mTagListRecylcerView.setHasFixedSize(true);
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

        mAddTagItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog pop up;
                CustomDialogForAddTagListItem();
            }
        });
        mAddTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //query -> tag list & tag
                getDataFromView();
                addTagToRealm();
                addTagListToRealm();
                finish();
            }
        });
    }

    public void CustomDialogForAddTagListItem() {

        final Calendar calendar = Calendar.getInstance();
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_addtag, null);
        final EditText edit_title = (EditText) dialogView.findViewById(R.id.dialog_tag_name);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.dialog_tag_timer);
        timePicker.setIs24HourView(true);

        if (Build.VERSION.SDK_INT >= 23) {
            timePicker.setHour(0);
            timePicker.setMinute(0);
        } else {
            timePicker.setCurrentHour(0);
            timePicker.setCurrentMinute(0);
        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
            }
        });


        AlertDialog.Builder buider = new AlertDialog.Builder(this);
        buider.setView(dialogView);
        buider.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= 23) {
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());

                } else {
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                }

                List<TagListObject> new_taglist = new ArrayList<TagListObject>();
                new_taglist.addAll(mTagListAdapter.getmTagList());

                mTagList = null;
                mTagList = new ArrayList<TagListObject>();
                mTagList.addAll(new_taglist);

                String s_title = edit_title.getText().toString();
                if (s_title.length() == 0) {
                    Toast.makeText(AddTagListActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                addTagListItem(s_title, new Date(calendar.getTimeInMillis()));
                mTagListAdapter.dataChagned(mTagList);
                //imm.hideSoftInputFromWindow(edit_title.getWindowToken(), 0);
            }
        });

        buider.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog dialog = buider.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void addTagListItem(String title, Date time){
        TagListObject obj = new TagListObject();
        obj.setTag_list_name(title);
        obj.setTag_date(time);
        obj.setTag_time_long(time.getTime());
        Log.d("ㅇㄴㅇㄴㅇㄴㅇ", "ㅇㄴㅇㄴㅇ");
        mTagList.add(obj);
        Log.d("after", mTagList.get(0).getTag_list_name());
    }


    public void addTagToRealm(){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                TagRealm mTag = bgRealm.createObject(TagRealm.class, mTagId);

                mTag.setTag_name(mTagTitle);

                bgRealm.insertOrUpdate(mTag);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d("REALM", "TAG sucess");
            }
        });

    }

    public void addTagListToRealm() {
        for (int i = 0; i < mTagList.size(); i++) {

            final int small_id=Utills.getNextKeyTagList(realm);
            final int idx = i;
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    TagListRealm mTagItem = bgRealm.createObject(TagListRealm.class, small_id);

                    mTagItem.setTag_id(mTagId);
                    mTagItem.setTag_date(mTagList.get(idx).getTag_date());
                    mTagItem.setTag_time_long(mTagList.get(idx).getTag_time_long());
                    mTagItem.setTag_order(idx);

                    bgRealm.insertOrUpdate(mTagItem);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d("REALM", "TAGLIST sucess");
                }
            });
        }
    }

    public void getDataFromView(){
        mTagTitle = mTagTitleView.getText().toString();
    }

}
