package org.totalit.sbms.domain;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

//@Entity(tableName = "procurement_docs" ,foreignKeys = @ForeignKey(entity = Client.class, parentColumns = "id",childColumns = "client_id"))
@Entity(tableName = "procurement_docs")
public class ProcumentDocs {
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
    @ColumnInfo(name = "application_letter")
    private String applicationLetter;

    @Expose
    @ColumnInfo(name = "company_profile")
    private String companyProfile;

    @Expose
    @ColumnInfo(name = "cert_of_incorporation")
    private String certOfIncorporation;

    @Expose
    @ColumnInfo(name = "mou")
    private String mou;

    @Expose
    @ColumnInfo(name = "cr_fourteen")
    private String crFourteen;

    @Expose
    @ColumnInfo(name = "vat_reg_cert")
    private String vat;

    @Expose
    @ColumnInfo(name = "itf")
    private String itf;

    @Expose
    @ColumnInfo(name = "trade_license")
    private String tradeLicense;

    @Expose
    @ColumnInfo(name = "traceable_ref")
    private String traceableReference;

    @Expose
    @ColumnInfo(name = "other")
    private String other;

    @Expose
    @ColumnInfo(name = "client_id")
    private int clientId;


    public ProcumentDocs(){

    }
    @Ignore
    public ProcumentDocs(int id, int realId, int syncStatus, String active, String dateCreated, String dateModified, int createdBy, int modifiedBy, String deleted, String applicationLetter, String companyProfile, String certOfIncorporation, String mou, String crFourteen, String vat, String itf, String tradeLicense, String traceableReference, String other, int clientId) {
        this.id = id;
        this.realId = realId;
        this.syncStatus = syncStatus;
        this.active = active;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.deleted = deleted;
        this.applicationLetter = applicationLetter;
        this.companyProfile = companyProfile;
        this.certOfIncorporation = certOfIncorporation;
        this.mou = mou;
        this.crFourteen = crFourteen;
        this.vat = vat;
        this.itf = itf;
        this.tradeLicense = tradeLicense;
        this.traceableReference = traceableReference;
        this.other = other;
        this.clientId = clientId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
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



    public String getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(String companyProfile) {
        this.companyProfile = companyProfile;
    }



    public String getMou() {
        return mou;
    }

    public void setMou(String mou) {
        this.mou = mou;
    }



    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getItf() {
        return itf;
    }

    public void setItf(String itf) {
        this.itf = itf;
    }

    public String getApplicationLetter() {
        return applicationLetter;
    }

    public void setApplicationLetter(String applicationLetter) {
        this.applicationLetter = applicationLetter;
    }

    public String getCertOfIncorporation() {
        return certOfIncorporation;
    }

    public void setCertOfIncorporation(String certOfIncorporation) {
        this.certOfIncorporation = certOfIncorporation;
    }

    public String getCrFourteen() {
        return crFourteen;
    }

    public void setCrFourteen(String crFourteen) {
        this.crFourteen = crFourteen;
    }

    public String getTradeLicense() {
        return tradeLicense;
    }

    public void setTradeLicense(String tradeLicense) {
        this.tradeLicense = tradeLicense;
    }

    public String getTraceableReference() {
        return traceableReference;
    }

    public void setTraceableReference(String traceableReference) {
        this.traceableReference = traceableReference;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
