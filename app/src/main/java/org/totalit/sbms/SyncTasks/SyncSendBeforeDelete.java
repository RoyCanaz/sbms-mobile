package org.totalit.sbms.SyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.SyncData.SyncAll;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

public class SyncSendBeforeDelete extends AsyncTask<Void, Void, Void> {
    AppDatabase mdb;
    Context context;
    int userId;
    SyncAll all;
    private final WeakReference<ProgressDialog> progressDialogWeakReference;

    public SyncSendBeforeDelete(AppDatabase mdb, ProgressDialog progressDialog, Context context, int userId) {
        this.mdb = mdb;
        this.context = context;
        this.userId = userId;
        this.progressDialogWeakReference = new WeakReference<>(progressDialog);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressDialog dialog = progressDialogWeakReference.get();
        dialog.setTitle("Sync");
        dialog.setMessage("Loading Please wait...");
        dialog.show();
        all = new SyncAll(mdb);
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
