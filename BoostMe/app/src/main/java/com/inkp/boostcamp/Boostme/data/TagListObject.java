package com.inkp.boostcamp.Boostme.data;

import java.util.Date;

/**
 * Created by inkp on 2017-02-21.
 */

public class TagListObject {
    private int tag_list_id;
    private  String tag_list_name;
    private int tag_id;
    private int tag_order;
    private long tag_time_long;
    private Date tag_date;

    public int getTag_list_id() {
        return tag_list_id;
    }

    public void setTag_list_id(int tag_list_id) {
        this.tag_list_id = tag_list_id;
    }

    public String getTag_list_name() {
        return tag_list_name;
    }

    public void setTag_list_name(String tag_list_name) {
        this.tag_list_name = tag_list_name;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public int getTag_order() {
        return tag_order;
    }

    public void setTag_order(int tag_order) {
        this.tag_order = tag_order;
    }

    public long getTag_time_long() {
        return tag_time_long;
    }

    public void setTag_time_long(long tag_time_long) {
        this.tag_time_long = tag_time_long;
    }

    public Date getTag_date() {
        return tag_date;
    }

    public void setTag_date(Date tag_date) {
        this.tag_date = tag_date;
    }
}
