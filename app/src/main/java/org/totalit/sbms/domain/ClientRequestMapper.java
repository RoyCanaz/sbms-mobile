package org.totalit.sbms.domain;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ClientRequestMapper {

    @Expose
    List<Client> clientList;
    @Expose
    List<Contact> contactList;
    @Expose
    List<Branch> branchList;
    @Expose
    List<ClientInventory> inventoryList;
    @Expose
    List<ProcumentDocs> procurementList;

    public ClientRequestMapper(List<Client> clientList, List<Contact> contactList, List<Branch> branchList, List<ClientInventory> inventoryList, List<ProcumentDocs> procurementList) {
        this.clientList = clientList;
        this.contactList = contactList;
        this.branchList = branchList;
        this.inventoryList = inventoryList;
        this.procurementList = procurementList;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public List<Branch> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<Branch> branchList) {
        this.branchList = branchList;
    }

    public List<ClientInventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<ClientInventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public List<ProcumentDocs> getProcurementList() {
        return procurementList;
    }

    public void setProcurementList(List<ProcumentDocs> procurementList) {
        this.procurementList = procurementList;
    }
}
