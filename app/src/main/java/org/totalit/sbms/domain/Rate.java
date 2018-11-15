package org.totalit.sbms.domain;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity
public class Rate {


    @Expose
    @PrimaryKey
    @ColumnInfo(name ="id")
    private int id;
    @Expose
    @ColumnInfo(name = "active")
    private Boolean active;
    @Expose
    @ColumnInfo(name = "name")
    private String name;

    @Expose
    @ColumnInfo(name = "rate")
    private Float rate;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return name+" ["+rate+"]";
    }
}
