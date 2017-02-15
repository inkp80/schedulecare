package com.inkp.boostcamp.Boostme;

import com.inkp.boostcamp.Boostme.data.ScheduleRealm;
import com.inkp.boostcamp.Boostme.data.SmallSchedule;
import com.inkp.boostcamp.Boostme.data.SmallScheduleRealm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by macbook on 2017. 2. 11..
 */

public class Utills{
    public static String access_Schedule_id = "schedule_id";
    public static int weekdays_requestCode=1313;
    public static int weekdays_resultCode=3131;

    public static SimpleDateFormat format_hhmm_a = new SimpleDateFormat("hh : mm a");

    public static SimpleDateFormat format_yymmdd_hhmm_a = new SimpleDateFormat("yy-MM-dd, hh:mm a");

    synchronized public static long getNextKeyMainSchedule(Realm realm)
    {
        if(realm.where(ScheduleRealm.class).max("id") == null)return 1;
        else
            return realm.where(ScheduleRealm.class).max("id").intValue() + 1;
    }
    synchronized public static long getNextKeySmallSchedule(Realm realm)
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

    public interface SmallScheduleInterface{
        public void isReleased(ArrayList<SmallSchedule> newData);
    }
}
