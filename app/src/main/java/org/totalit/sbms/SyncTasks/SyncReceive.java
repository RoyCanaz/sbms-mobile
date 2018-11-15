package org.totalit.sbms.SyncTasks;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.HomeActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.SyncData.SyncGetAll;
import org.totalit.sbms.fragments.ClientManagementHomeFragment;

import java.lang.ref.WeakReference;

public class SyncReceive extends AsyncTask<Void, Void, Void> {
    AppDatabase mdb;
    int userId;
    Context context;
    SyncGetAll syncGetAll;
    private final WeakReference<ProgressDialog> progressDialogWeakReference;

    public SyncReceive(AppDatabase mdb, ProgressDialog progressDialog, int userId, Context context) {
        this.mdb = mdb;
        this.userId = userId;
        this.context = context;
        this.progressDialogWeakReference = new WeakReference<>(progressDialog);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected void onPostExecute(Void aVoid) {

         if(progressDialogWeakReference.get()!=null && progressDialogWeakReference.get().isShowing()){
             goC();
             progressDialogWeakReference.get().dismiss();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        syncGetAll = new SyncGetAll(mdb, context, userId);
        syncGetAll.getClientData();
        return null;
    }
    private void goC(){


        Fragment fragment = null;
        fragment = new ClientManagementHomeFragment();
        //homeActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
        ((HomeActivity)context).replaceFragement(fragment);
       // homeActivity.replaceFragement(fragment);
        Toast.makeText(context, "Sync Successful", Toast.LENGTH_LONG).show();
    }
}
