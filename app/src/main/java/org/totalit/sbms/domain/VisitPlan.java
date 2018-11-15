package org.totalit.sbms.domain;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "visit_plan")
public class VisitPlan {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="id")
    private int id;

    @ColumnInfo(name ="real_id")
    private int realId;

    @ColumnInfo(name = "sync_status")
    private int syncStatus;

    @ColumnInfo(name="active")
    private String active;

    @ColumnInfo(name = "date_created")
    private String dateCreated;

    @ColumnInfo(name = "date_modified")
    private String dateModified;

    @ColumnInfo(name = "created_by")
    private int createdBy;

    @ColumnInfo(name = "modified_by")
    private int modifiedBy;

    @ColumnInfo(name = "deleted")
    private String deleted;

    @ColumnInfo(name = "date_of_visit")
    private String dateOfVisit;

    @ColumnInfo(name = "visit_result")
    private String visitResult;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "client_id")
    private int clientId;

    public VisitPlan() {
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

    public String getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(String dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getVisitResult() {
        return visitResult;
    }

    public void setVisitResult(String visitResult) {
        this.visitResult = visitResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
