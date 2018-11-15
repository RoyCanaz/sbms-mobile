package org.totalit.sbms.domain;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity
public class Bank {


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
    @ColumnInfo(name = "bank")
    private String bank;
    @Expose
    @ColumnInfo(name = "account_number")
    private String accountNumber;
    @Expose
    @ColumnInfo(name = "branch")
    private String branch;

    public Bank() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return bank;
    }
}
