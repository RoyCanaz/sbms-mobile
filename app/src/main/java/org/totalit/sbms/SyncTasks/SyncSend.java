package org.totalit.sbms.SyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.SyncData.SyncAll;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class SyncSend extends AsyncTask<Void, Void, Void> {
    AppDatabase mdb;
    int userId;
    Context context;
    private final WeakReference<ProgressDialog> progressDialogWeakReference;

    public SyncSend(AppDatabase mdb, ProgressDialog progressDialog, int userId, Context context) {
        this.mdb = mdb;
        this.userId = userId;
        this.context = context;
        this.progressDialogWeakReference = new WeakReference<>(progressDialog);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        SyncAll all = new SyncAll(mdb);
        try {
            all.syncBranches();
            all.syncClientInventory();
            all.syncContacts();
            all.syncProcurementDocs();
            all.syncNotes();
            all.syncVisitPlan();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ProgressDialog progressDialog = progressDialogWeakReference.get();
        AsyncTask<Void, Void, Void> asyncTask = new SyncReceive(mdb, progressDialog, userId, context);
        asyncTask.execute();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        return null;
    }
}
