package org.totalit.sbms.SyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Client;

import java.lang.ref.WeakReference;

public class UpdateClientTask extends AsyncTask<Void, Void, Wrapper> {
    AppDatabase mdb;
    Client client;
    //ProgressDialog progressDialog;
    Context context;
    private final WeakReference<ProgressDialog> progressDialogWeakReference;
    int userId;



    public UpdateClientTask(AppDatabase mdb, Client client, ProgressDialog progressDialog, Context context, int userId) {
        this.mdb = mdb;
        this.client = client;
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
    }

    @Override
        protected Wrapper doInBackground(Void... voids) {
         mdb.clientRepository().deleteClient();
            Wrapper wrapper = new Wrapper();
            wrapper.oldClientId = client.getId();
            wrapper.newClientId = client.getRealId();
            return wrapper;
        }

    @Override
    protected void onPostExecute(Wrapper wrapper) {
        super.onPostExecute(wrapper);
        ProgressDialog dialog = progressDialogWeakReference.get();
        AsyncTask<Integer, Void, Void> asyncTask = new UpdateClientDetails(mdb, dialog, context, userId);
        asyncTask.execute(wrapper.oldClientId, wrapper.newClientId);
    }
}
