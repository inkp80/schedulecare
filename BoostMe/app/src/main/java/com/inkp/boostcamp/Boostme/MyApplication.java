package com.inkp.boostcamp.Boostme;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by macbook on 2017. 2. 10..
 */

public class MyApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
    }
}
