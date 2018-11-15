package org.totalit.sbms.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;


@Entity
public class Category {
    @Expose
    @PrimaryKey
    @ColumnInfo(name ="id")
    private int id;
    @Expose
    @ColumnInfo(name = "name")
    private String name;
    @Expose
    @ColumnInfo(name = "description")
    private String description;

    public Category() {
    }

    @Ignore
    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
