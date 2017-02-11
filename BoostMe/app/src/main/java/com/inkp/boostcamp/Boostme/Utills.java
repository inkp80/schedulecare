package com.inkp.boostcamp.Boostme;

import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import io.realm.Realm;

/**
 * Created by macbook on 2017. 2. 11..
 */

public class Utills {
    synchronized public static int getNextKeyMainSchedule(Realm realm)
    {
        if(realm.where(ScheduleRealm.class).max("id") == null)return 1;
        else
            return realm.where(ScheduleRealm.class).max("id").intValue() + 1;
    }
    synchronized public static int getNextKeySmallSchedule(Realm realm)
    {
        if(realm.where(SmallScheduleRealm.class).max("id") == null)return 1;
        else
            return realm.where(SmallScheduleRealm.class).max("id").intValue() + 1;
    }
}
