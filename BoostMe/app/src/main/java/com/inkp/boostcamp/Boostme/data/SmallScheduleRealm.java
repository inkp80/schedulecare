package com.inkp.boostcamp.Boostme.data;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by macbook on 2017. 2. 10..
 */

public class SmallScheduleRealm extends RealmObject {

    @PrimaryKey
    private int id;
    private int schedule_id;
    private String small_tilte;
    private Date small_time;
    private int order_value;
    private boolean alarm_flag;

    private long alarm_start_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public int getOrder_value() {
        return order_value;
    }

    public void setOrder_value(int order_value) {
        this.order_value = order_value;
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

    public void setAlarm_flag(boolean alarm_flag) {
        this.alarm_flag = alarm_flag;
    }

    public long getAlarm_start_time() {
        return alarm_start_time;
    }

    public void setAlarm_start_time(long alarm_start_time) {
        this.alarm_start_time = alarm_start_time;
    }
}
