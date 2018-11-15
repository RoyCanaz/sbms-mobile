package org.totalit.sbms.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/*@Entity(tableName = "branch", foreignKeys = @ForeignKey(entity = Client.class,
        parentColumns = "id",
        childColumns = "client_id",
        onDelete = CASCADE))*/
@Entity
public class Branch {
    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="id")
    private int id;

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
    @ColumnInfo(name = "real_id")
    private int realId;

    @Expose
    @ColumnInfo(name = "sync_status")
    private int syncStatus;

    @Expose
    @ColumnInfo(name = "name")
    private String branch;

    @Expose
    @ColumnInfo(name = "description")
    private String description;


    @Expose
    @ColumnInfo(name = "address")
    private String address;
    @Expose
    @ColumnInfo(name = "client_id")
    private int clientId;



    public Branch(){

    }


    @Ignore
    public Branch(int id, String active, String dateCreated, String dateModified, int createdBy, int modifiedBy, String deleted, int realId, int syncStatus, String branch, String description, String address, int clientId) {
        this.id = id;
        this.active = active;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.deleted = deleted;
        this.realId = realId;
        this.syncStatus = syncStatus;
        this.branch = branch;
        this.description = description;
        this.address = address;
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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
