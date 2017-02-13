package com.inkp.boostcamp.Boostme;

import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import io.realm.Realm;

/**
 * Created by macbook on 2017. 2. 11..
 */

public class Utills {
    public static int weekdays_requestCode=1313;
    public static int weekdays_resultCode=3131;
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

    public static int checkTargetWeekOfDayIsSet(int val, int Target_WeekOfDay){
        int flag = val & (1 << Target_WeekOfDay);
        if(flag!=0)
            return (1<<Target_WeekOfDay);
        else return 0;
    }
}
