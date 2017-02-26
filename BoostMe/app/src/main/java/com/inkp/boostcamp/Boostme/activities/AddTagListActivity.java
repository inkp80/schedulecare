package com.inkp.boostcamp.Boostme.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
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
import io.realm.RealmResults;

import static com.inkp.boostcamp.Boostme.Utills.ONE_MINUTE_IN_MILLIS;

/**
 * Created by inkp on 2017-02-20.
 */

public class AddTagListActivity extends AppCompatActivity {
    public TagListAdapter mTagListAdapter;
    RecyclerView mTagListRecylcerView;
    RealmResults<TagListRealm> mTagListRealm;
    List<TagListObject> mTagList;
    Realm realm;

    int mActionFlag = 0;

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
    @BindView(R.id.toolbar_addtag_delete)
    ImageButton mDeleteTagButton;


    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_add_tag);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.tag_add_toolbar));


        mTagListRecylcerView = (RecyclerView) findViewById(R.id.addtag_taglist_recyclerview);

        calendar = new GregorianCalendar();
        date = new Date();
        realm = Realm.getDefaultInstance();
        mTagId = Utills.getNextKeyTag(realm);
        mTagList = new ArrayList<>();


        Intent intent = getIntent();
        mActionFlag = intent.getIntExtra(Utills.Tag_intent_action, 0);
        if (mActionFlag == 1) {
            mTagId = intent.getIntExtra(Utills.TAG_intent_tagid, 0);
            mTagTitleView.setText(intent.getStringExtra(Utills.Tag_intent_title));
            mTagListRealm = realm.where(TagListRealm.class).equalTo("tag_id", mTagId).findAllSorted("tag_order");
            for (int i = 0; i < mTagListRealm.size(); i++) {
                TagListObject tmp = new TagListObject();
                tmp.setTag_list_id(mTagListRealm.get(i).getId());
                tmp.setTag_id(mTagListRealm.get(i).getTag_id());
                tmp.setTag_date(mTagListRealm.get(i).getTag_date());
                tmp.setTag_time_long(mTagListRealm.get(i).getTag_time_long());
                tmp.setTag_list_name(mTagListRealm.get(i).getTag_list_name());
                mTagList.add(tmp);
            }
        }


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
        mDeleteTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActionFlag == 1) {
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            TagRealm mTag;
                            mTag = bgRealm.where(TagRealm.class).equalTo("id", mTagId).findFirst();
                            //mTagListRealm.deleteAllFromRealm();
                            mTag.deleteFromRealm();
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Log.d("REALM", "TAG sucess");
                        }
                    });
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            mTagListRealm.deleteAllFromRealm();
                        }
                    });
                }
                finish();
            }
        });

        initKeyBoard();
    }

    public void CustomDialogForAddTagListItem() {

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);


        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_addtag, null);
        final EditText edit_title = (EditText) dialogView.findViewById(R.id.dialog_tag_name);
        final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.dialog_tag_timer);

        final Button add_5min = (Button) dialogView.findViewById(R.id.tag_dialog_small_5min);
        final Button add_15min = (Button) dialogView.findViewById(R.id.tag_dialog_small_15min);
        final Button add_30min = (Button) dialogView.findViewById(R.id.tag_dialog_small_30min);


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


        add_5min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long cal_time = calendar.getTimeInMillis() + (5 * Utills.ONE_MINUTE_IN_MILLIS);
                calendar.setTimeInMillis(cal_time);
                if (Build.VERSION.SDK_INT >= 23) {
                    timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                    if (( (5 + timePicker.getMinute()) * ONE_MINUTE_IN_MILLIS) >= (60 * Utills.ONE_MINUTE_IN_MILLIS)) {
                        cal_time = (cal_time + (timePicker.getMinute() * ONE_MINUTE_IN_MILLIS)) - 60 * ONE_MINUTE_IN_MILLIS;
                        calendar.setTimeInMillis(cal_time);
                        timePicker.setMinute((5+timePicker.getMinute())-60);
                    } else {
                        timePicker.setMinute(calendar.get(Calendar.MINUTE));
                    }
                } else {
                    timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                    timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                }
            }
        });

        add_15min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long cal_time = calendar.getTimeInMillis() + (15*60*1000);
                calendar.setTimeInMillis(cal_time);

                if (Build.VERSION.SDK_INT >= 23) {
                    timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                    if (( (15 + timePicker.getMinute()) * ONE_MINUTE_IN_MILLIS) >= (60 * Utills.ONE_MINUTE_IN_MILLIS)) {
                        cal_time = (cal_time + (timePicker.getMinute() * ONE_MINUTE_IN_MILLIS)) - 60 * ONE_MINUTE_IN_MILLIS;
                        calendar.setTimeInMillis(cal_time);
                        timePicker.setMinute((15+timePicker.getMinute())-60);
                    } else {
                        timePicker.setMinute(calendar.get(Calendar.MINUTE));
                    }
                } else {
                    timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                    timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                }
            }
        });

        add_30min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long cal_time = calendar.getTimeInMillis() + (30*60*1000);
                calendar.setTimeInMillis(cal_time);
                if (Build.VERSION.SDK_INT >= 23) {
                    timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                    if (( (30 + timePicker.getMinute()) * ONE_MINUTE_IN_MILLIS) >= (60 * Utills.ONE_MINUTE_IN_MILLIS)) {
                        cal_time = (cal_time + (timePicker.getMinute() * ONE_MINUTE_IN_MILLIS)) - 60 * ONE_MINUTE_IN_MILLIS;
                        calendar.setTimeInMillis(cal_time);
                        timePicker.setMinute((30+timePicker.getMinute())-60);
                    } else {
                        timePicker.setMinute(calendar.get(Calendar.MINUTE));
                    }
                } else {
                    timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                    timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
                }
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
                imm.hideSoftInputFromWindow(edit_title.getWindowToken(), 0);
            }
        });

        buider.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
                imm.hideSoftInputFromWindow(edit_title.getWindowToken(), 0);
            }
        });

        AlertDialog dialog = buider.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void addTagListItem(String title, Date time) {
        TagListObject obj = new TagListObject();
        obj.setTag_list_name(title);
        obj.setTag_date(time);
        obj.setTag_time_long(time.getTime());
        mTagList.add(obj);
    }


    public void addTagToRealm() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                TagRealm mTag;
                if (mActionFlag == 1) {
                    mTag = bgRealm.where(TagRealm.class).equalTo("id", mTagId).findFirst();
                } else {
                    mTag = bgRealm.createObject(TagRealm.class, mTagId);
                }
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

        if (mActionFlag == 1) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mTagListRealm.deleteAllFromRealm();
                }
            });
        }
        for (int i = 0; i < mTagList.size(); i++) {

            //final int small_id=Utills.getNextKeyTagList(realm);
            final int idx = i;
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm bgRealm) {
                    TagListRealm mTagItem = bgRealm.createObject(TagListRealm.class, Utills.getNextKeyTagList(bgRealm));
                    mTagItem.setTag_id(mTagId);
                    mTagItem.setTag_date(mTagList.get(idx).getTag_date());
                    mTagItem.setTag_time_long(mTagList.get(idx).getTag_time_long());
                    mTagItem.setTag_list_name(mTagList.get(idx).getTag_list_name());
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

    public void getDataFromView() {
        mTagTitle = mTagTitleView.getText().toString();
    }

    private void initKeyBoard() {
        imm = (InputMethodManager) getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
    }
}
