package com.inkp.boostcamp.Boostme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.inkp.boostcamp.Boostme.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by inkp on 2017-02-20.
 */

public class ShowTagActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_tag_add_button)
    ImageButton mTagAddButton;

    @Override
    public void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.activity_show_tag);
        ButterKnife.bind(this);
        mTagAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowTagActivity.this, AddTagListActivity.class);
                startActivity(intent);
            }
        });
    }
}