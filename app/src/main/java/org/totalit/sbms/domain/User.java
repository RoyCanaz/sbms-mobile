package org.totalit.sbms.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import java.util.Date;

@Entity
public class User {
    @Expose
    @PrimaryKey
    private int id;

    @Expose
    @ColumnInfo(name = "active")
    private Boolean active;
    @Expose
    @ColumnInfo(name = "user_name")
    private String userName;
    @Expose
    @ColumnInfo(name = "first_name")
    private String firstName;
    @Expose
    @ColumnInfo(name = "last_name")
    private String lastName;
    @Expose
    @ColumnInfo(name = "password")
    private String password;
    @Expose
    @ColumnInfo(name = "company")
    private int companyId;
    @Expose
    @ColumnInfo(name = "companyName")
    private String companyName;


    @Expose
    @ColumnInfo(name = "role")
    private String role;


    public User() {

    }
     @Ignore
    public User(int id, Boolean active, String userName, String firstName, String lastName, String password, int companyId, String companyName, String role) {
        this.id = id;
        this.active = active;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.companyId = companyId;
        this.role = role;
        this.companyName = companyName;
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
