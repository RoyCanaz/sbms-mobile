package org.totalit.sbms.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

//@Entity(tableName = "client_inventory", foreignKeys = @ForeignKey(entity = Client.class, parentColumns="id", childColumns="client_id"))
@Entity(tableName = "client_inventory")
public class ClientInventory {
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
    @ColumnInfo(name = "category")
    private int category;

    @ColumnInfo(name = "model")
    private String model;

    @ColumnInfo(name = "toner_type")
    private String tonerType;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "need_maintenance")
    private String needMaintenence;

    @ColumnInfo(name = "client_id")
    private int clientId;

    public ClientInventory(){

    }

    @Ignore
    public ClientInventory(int id, int realId, int syncStatus, String active, String dateCreated, String dateModified, int createdBy, int modifiedBy, String deleted, int category, String model, String description, String tonerType, int quantity, String needMaintenence, int clientId) {
        this.id = id;
        this.realId = realId;
        this.syncStatus = syncStatus;
        this.active = active;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.deleted = deleted;
        this.category = category;
        this.model = model;
        this.description = description;
        this.quantity = quantity;
        this.tonerType = tonerType;
        this.needMaintenence = needMaintenence;
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


    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTonerType() {
        return tonerType;
    }

    public void setTonerType(String tonerType) {
        this.tonerType = tonerType;
    }



    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNeedMaintenence() {
        return needMaintenence;
    }

    public void setNeedMaintenence(String needMaintenence) {
        this.needMaintenence = needMaintenence;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
