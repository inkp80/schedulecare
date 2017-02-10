package com.inkp.boostcamp.Boostme.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by inkp on 2017-02-10.
 */

public class Schedule extends RealmObject{

    @PrimaryKey
    private String id;

    private String title;
    private Date date;
    private String location;
    private int Week_of_day_repit;
    private int alarm_flag;

    @Ignore
    private int sessionId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getWeek_of_day_repit() {
        return Week_of_day_repit;
    }

    public void setWeek_of_day_repit(int week_of_day_repit) {
        Week_of_day_repit = week_of_day_repit;
    }

    public int getAlarm_flag() {
        return alarm_flag;
    }

    public void setAlarm_flag(int alarm_flag) {
        this.alarm_flag = alarm_flag;
    }


}