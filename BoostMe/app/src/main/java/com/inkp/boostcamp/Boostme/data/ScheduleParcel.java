package com.inkp.boostcamp.Boostme.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import io.realm.RealmList;

/**
 * Created by inkp on 2017-02-15.
 */

public class ScheduleParcel implements Parcelable {
    private long ids;
    private String titles;
    private long dateInLong;
    private int week_of_dates;
    private int isAlarm; //0 is off 1 is on

    public ScheduleParcel(Parcel in){
        readFromParcel(in);
    }
    public ScheduleParcel(long id, String titleIn, long dateIn, int weekIn, int alarmFlagIn){
        this.ids=id;
        this.titles=titleIn;
        this.dateInLong=dateIn;
        this.week_of_dates=weekIn;
        this.isAlarm=alarmFlagIn;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(ids);
        dest.writeString(titles);
        dest.writeLong(dateInLong);
        dest.writeInt(week_of_dates);
        dest.writeInt(isAlarm);
    }

    public void readFromParcel(Parcel in){

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public ScheduleParcel createFromParcel (Parcel in){
            return new ScheduleParcel(in);
        }
        public ScheduleParcel[] newArray(int size){
            return new ScheduleParcel[size];
        }
    };


    public long getIds() {
        return ids;
    }

    public void setIds(long ids) {
        this.ids = ids;
    }

    public String getTitles() {
        return titles;
    }

    public void setTitles(String titles) {
        this.titles = titles;
    }

    public long getDateInLong() {
        return dateInLong;
    }

    public void setDateInLong(long dateInLong) {
        this.dateInLong = dateInLong;
    }

    public int getWeek_of_dates() {
        return week_of_dates;
    }

    public void setWeek_of_dates(int week_of_dates) {
        this.week_of_dates = week_of_dates;
    }

    public int getIsAlarm() {
        return isAlarm;
    }

    public void setIsAlarm(int isAlarm) {
        this.isAlarm = isAlarm;
    }
}
