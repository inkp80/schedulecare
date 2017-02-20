package com.inkp.boostcamp.Boostme.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by inkp on 2017-02-20.
 */

public class TagRealm extends RealmObject {
    @PrimaryKey
    private int tag_id;
    private  String tag_name;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
}
