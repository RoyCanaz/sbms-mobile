package org.totalit.sbms.SyncData;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.R;
import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.Notes;
import org.totalit.sbms.domain.ProcumentDocs;
import org.totalit.sbms.domain.VisitPlan;

public class UpdateFromResponse {
    AppDatabase mdb;

    public UpdateFromResponse(AppDatabase mdb) {
        this.mdb = mdb;
    }
    public void updateProcDocs(final ProcumentDocs procumentDocs){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.procumentDocsDao().updateProcurement(procumentDocs);
            }
        });


    }
    public void updateBranch(final Branch branch){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.branchDao().updateBranch(branch);
            }
        });
    }
    public void updateContact(final Contact contact){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.contactDao().updateContact(contact);
            }
        });
    }
    public void updateClientIn(final ClientInventory clientInventory){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.clientInventoryDao().update(clientInventory);
            }
        });
    }
    public void updateNotes(final Notes note){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.notesDao().updateNote(note);
            }
        });
    }
    public void updateVisit(final VisitPlan visitPlan ){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.visitPlanDao().updateVisitPlan(visitPlan);
            }
        });
    }
}
