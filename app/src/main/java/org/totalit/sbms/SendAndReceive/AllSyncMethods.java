package org.totalit.sbms.SendAndReceive;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.ProcumentDocs;

import java.util.ArrayList;
import java.util.List;

public class AllSyncMethods {
     AppDatabase mdb;
    public AllSyncMethods(AppDatabase mdb) {
        this.mdb = mdb;
    }
/*
    public List<Branch> getNotSyncedBranch(){
        List<Branch> branches = new ArrayList<>();
        branches = mdb.branchDao().getAllNotSynced();
        return branches;

    }
    public List<Client> getNotSyncedClient(){
        List<Client> client = new ArrayList<>();
        client = mdb.clientRepository().getAllNotSynced();

        return client;
    }
    public List<ClientInventory> getNotSyncedInventory(){
        List<ClientInventory> clientInventory = new ArrayList<>();
        clientInventory = mdb.clientInventoryDao().getAllNotSynced();

        return clientInventory;
    }
    public List<Contact> getNotSyncedContact(){
        List<Contact> con = new ArrayList<>();
      //  con = mdb.contactDao().getAllNotSynced();
        return con;

    }
    public List<ProcumentDocs> getNotSyncedDocs(){
        List<ProcumentDocs> procumentDocs = new ArrayList<>();
        procumentDocs = mdb.procumentDocsDao().getAllNotSynced();
        return procumentDocs;
    }
    */
}
