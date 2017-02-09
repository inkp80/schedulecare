package com.inkp.boostcamp.Boostme;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;

/**
 * Created by macbook on 2017. 2. 9..
 */

public class AddTaskActivity extends AppCompatActivity {
    @BindView(R.id.add_title_text)
    TextView titleView;
    @BindView(R.id.)

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_add_task);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar_color)));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.custom_actionbar_add, null);

        actionBar.setCustomView(actionbar);
        Toolbar parent = (Toolbar) actionbar.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        View view = getSupportActionBar().getCustomView();
        ImageView saveButton = (ImageView) view.findViewById(R.id.add_save_button);
        saveButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){

                    }
                }
        );

        final ImageView saveListButton = (ImageView) view.findViewById(R.id.add_save_button);

        return true;
    }


    public void getDataFromView(){


    }
}
