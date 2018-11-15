package org.totalit.sbms.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity
public class Client {
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
    @ColumnInfo(name = "client_type")
    private String clientType;

    @Expose
    @ColumnInfo(name = "name")
    private String name;



    @Expose
    @ColumnInfo(name = "description")
    private String description;


    @Expose
    @ColumnInfo(name = "website")
    private String website;

    @Expose
    @ColumnInfo(name = "address")
    private String address;

    @Expose
    @ColumnInfo(name = "email")
    private String email;

    @Expose
    @ColumnInfo(name = "phone")
    private String phone;

    @Expose
    @ColumnInfo(name ="branches")
    private String branch;

    @Expose
    private Long companyId;



    public Client(){

    }
   @Ignore
    public Client(int id, int realId, int syncStatus, String active, String dateCreated, String dateModified, int createdBy, int modifiedBy, String deleted, String clientType, String name, String description, String website, String address, String email, String phone, String branch) {
        this.id = id;
        this.realId = realId;
        this.syncStatus = syncStatus;
        this.active = active;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.deleted = deleted;
        this.clientType = clientType;
        this.name = name;
        this.description = description;
        this.website = website;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.branch = branch;
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

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public String toString() {
        return name;
    }
}
