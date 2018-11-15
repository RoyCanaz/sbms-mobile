package org.totalit.sbms.domain;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

//@Entity(tableName = "contact", foreignKeys = @ForeignKey(entity = Client.class, parentColumns="id", childColumns="client_id"))
@Entity(tableName = "contact")
public class Contact {

    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="id")
    private int id;

    @Expose
    @ColumnInfo(name ="real_id")
    private int realId;

    @Expose
    @ColumnInfo(name = "sync_status")
    private int syncStatus;

    @Expose
    @ColumnInfo(name="active")
    private String active;

    @Expose
    @ColumnInfo(name = "date_created")
    private String dateCreated;

    @Expose
    @ColumnInfo(name = "date_modified")
    private String dateModified;

    @Expose
    @ColumnInfo(name = "created_by")
    private int createdBy;

    @Expose
    @ColumnInfo(name = "modified_by")
    private int modifiedBy;

    @Expose
    @ColumnInfo(name = "deleted")
    private String deleted;

    @Expose
    @ColumnInfo(name = "first_name")
    private String firstName;

    @Expose
    @ColumnInfo(name = "last_name")
    private String lastName;


    @Expose
    @ColumnInfo(name = "gender")
    private String gender;

    @Expose
    @ColumnInfo(name = "job_position")
    private String jobPosition;

    @Expose
    @ColumnInfo(name = "department")
    private String department;

    @Expose
    @ColumnInfo(name = "office_phone")
    private String officePhone;

    @Expose
    @ColumnInfo(name = "mobile_phone")
    private String mobilePhone;

    @Expose
    @ColumnInfo(name = "email")
    private String email;

    @Expose
    @ColumnInfo(name = "client_id")
    private int clientId;

    public Contact() {

    }

    @Ignore
    public Contact(int id, int realId, int syncStatus, String active, String dateCreated, String dateModified, int createdBy, int modifiedBy, String deleted, String firstName, String lastName, String gender, String jobPosition, String department, String officePhone, String mobilePhone, String email, int clientId) {
        this.id = id;
        this.realId = realId;
        this.syncStatus = syncStatus;
        this.active = active;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.deleted = deleted;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.jobPosition = jobPosition;
        this.department = department;
        this.officePhone = officePhone;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.clientId = clientId;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRealId() {
        return realId;
    }

    public void setRealId(int realId) {
        this.realId = realId;
    }

    public int getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return  firstName +" "+lastName+"("+email+")";
    }
}
