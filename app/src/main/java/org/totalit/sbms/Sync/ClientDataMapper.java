package org.totalit.sbms.Sync;

import com.google.gson.annotations.Expose;

import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.Notes;
import org.totalit.sbms.domain.ProcumentDocs;
import org.totalit.sbms.domain.VisitPlan;

import java.util.List;

public class ClientDataMapper {
    @Expose
    Client client;
    @Expose
    ProcumentDocs document;
    @Expose
    List<Contact> contactList;
    @Expose
    List<Branch> branchList;
    @Expose
    List<ClientInventory> clientInventoryList;
    @Expose
    List<VisitPlan> visitPlanList;
    @Expose
    List<Notes> noteList;

    public ClientDataMapper() {
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ProcumentDocs getDocument() {
        return document;
    }

    public void setDocument(ProcumentDocs document) {
        this.document = document;
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

    public List<ClientInventory> getClientInventoryList() {
        return clientInventoryList;
    }

    public void setClientInventoryList(List<ClientInventory> clientInventoryList) {
        this.clientInventoryList = clientInventoryList;
    }

    public List<VisitPlan> getVisitPlanList() {
        return visitPlanList;
    }

    public void setVisitPlanList(List<VisitPlan> visitPlanList) {
        this.visitPlanList = visitPlanList;
    }

    public List<Notes> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Notes> noteList) {
        this.noteList = noteList;
    }
}
