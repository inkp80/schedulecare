package com.inkp.boostcamp.Boostme.data;

import java.util.Date;

import io.realm.annotations.PrimaryKey;

/**
 * Created by macbook on 2017. 2. 10..
 */

public class SmallSchedule {
    private String small_tilte;
    private Date small_time;
    private int order_value;

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



}
