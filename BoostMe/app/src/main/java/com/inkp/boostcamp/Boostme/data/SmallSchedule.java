package com.inkp.boostcamp.Boostme.data;

import java.util.Date;

import io.realm.annotations.PrimaryKey;

/**
 * Created by macbook on 2017. 2. 10..
 */

public class SmallSchedule {
    private String small_tilte;
    private Date small_time;
    private long small_time_long;
    private long end_time;
    private long alert_time;
    private boolean alarm_flag;
    private boolean depart_time;

    public long getSmall_time_long() {
        return small_time_long;
    }

    public void setSmall_time_long(long small_time_long) {
        this.small_time_long = small_time_long;
    }

    public boolean isDepart_time() {
        return depart_time;
    }

    public void setDepart_time(boolean depart_time) {
        this.depart_time = depart_time;
    }

    public Date getSmall_time() {
        return small_time;
    }

    public void setSmall_time(Date small_time) {
        this.small_time = small_time;
    }

    public String getSmall_tilte() {
        return small_tilte;
    }

    public void setSmall_tilte(String small_tilte) {
        this.small_tilte = small_tilte;
    }

    public boolean isAlarm_flag() {
        return alarm_flag;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public long getAlert_time() {
        return alert_time;
    }

    public void setAlert_time(long alert_time) {
        this.alert_time = alert_time;
    }

    public void setAlarm_flag(boolean alarm_flag) {
        this.alarm_flag = alarm_flag;
    }
}
