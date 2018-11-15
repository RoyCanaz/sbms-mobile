package org.totalit.sbms.SyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.HomeActivity;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.SendAndReceive.AllSyncMethods;
import org.totalit.sbms.SendAndReceive.BranchData;
import org.totalit.sbms.SendAndReceive.ClientInventoryData;
import org.totalit.sbms.SendAndReceive.ContactsData;
import org.totalit.sbms.SendAndReceive.ProcurementDocsData;
import org.totalit.sbms.SyncData.SyncAll;
import org.totalit.sbms.SyncData.SyncGetAll;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.ProcumentDocs;
import org.totalit.sbms.fragments.ClientHome;
import org.totalit.sbms.fragments.ClientManagementHomeFragment;
import org.totalit.sbms.retrofit.BranchService;
import org.totalit.sbms.retrofit.CategoryService;
import org.totalit.sbms.retrofit.ClientInventoryService;
import org.totalit.sbms.retrofit.ClientService;
import org.totalit.sbms.retrofit.ContactService;
import org.totalit.sbms.retrofit.ProcurementDocsservice;
import org.totalit.sbms.retrofit.RetrofitConfig;
import org.totalit.sbms.retrofit.UserService;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateClientDetails extends AsyncTask<Integer, Void, Void> {
    AppDatabase mdb;
    ClientInventoryService clientInventoryService;
    ProcurementDocsservice docsservice;
    ContactService contactService;
    BranchService branchService;
    Retrofit retrofit;
    private final WeakReference<ProgressDialog> progressDialogWeakReference;
    Context context;
    int userId;

    public UpdateClientDetails(AppDatabase mdb, ProgressDialog progressDialog, Context context, int userId) {
        this.mdb = mdb;
        retrofit = RetrofitConfig.retrofitConfig();
        clientInventoryService = retrofit.create(ClientInventoryService.class);
        contactService = retrofit.create(ContactService.class);
        branchService = retrofit.create(BranchService.class);
        docsservice = retrofit.create(ProcurementDocsservice.class);
        this.progressDialogWeakReference = new WeakReference<>(progressDialog);
        this.context  = context;
        this.userId = userId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        mdb.clientInventoryDao().updateAllClientInventory(integers[0], integers[1]);
        mdb.branchDao().updateAllBranches(integers[0], integers[1]);
        mdb.contactDao().updateAllContacts(integers[0], integers[1]);
        mdb.procumentDocsDao().updateProcurement(integers[0], integers[1]);
        mdb.notesDao().updateAllNotes(integers[0], integers[1]);
        mdb.visitPlanDao().updateAllVisitPlan(integers[0], integers[1]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ProgressDialog progressDialog = progressDialogWeakReference.get();
         AsyncTask<Void, Void, Void> asyncTask = new SyncSend(mdb, progressDialog,  userId, context);
         asyncTask.execute();

    }



}
