package org.totalit.sbms.domain.temp;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;

@Entity
public class QuoteTemp {

    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name ="id")
    private int id;
    @Expose
    private String quoteUuid;
    @Expose
    private Double total;
    @Expose
    private int numOfItems;
    @Expose
    private int countNumberOfSent = 0;
    @Expose
    private Double vat;
    @Expose
    private Double totalIncVat;
    @Expose
    private int lastSendMailStatus = 0;

    @Expose
    private Long contact;
    @Expose
    private Long currency;
    @Expose
    private Long client;
    @Expose
    private Long bankingDetail;
    @Expose
    private Long company;


    public QuoteTemp() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuoteUuid() {
        return quoteUuid;
    }

    public void setQuoteUuid(String quoteUuid) {
        this.quoteUuid = quoteUuid;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public int getNumOfItems() {
        return numOfItems;
    }

    public void setNumOfItems(int numOfItems) {
        this.numOfItems = numOfItems;
    }

    public int getCountNumberOfSent() {
        return countNumberOfSent;
    }

    public void setCountNumberOfSent(int countNumberOfSent) {
        this.countNumberOfSent = countNumberOfSent;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getTotalIncVat() {
        return totalIncVat;
    }

    public void setTotalIncVat(Double totalIncVat) {
        this.totalIncVat = totalIncVat;
    }

    public int getLastSendMailStatus() {
        return lastSendMailStatus;
    }

    public void setLastSendMailStatus(int lastSendMailStatus) {
        this.lastSendMailStatus = lastSendMailStatus;
    }

    public Long getContact() {
        return contact;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }

    public Long getCurrency() {
        return currency;
    }

    public void setCurrency(Long currency) {
        this.currency = currency;
    }

    public Long getClient() {
        return client;
    }

    public void setClient(Long client) {
        this.client = client;
    }

    public Long getBankingDetail() {
        return bankingDetail;
    }

    public void setBankingDetail(Long bankingDetail) {
        this.bankingDetail = bankingDetail;
    }

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }
}
