package org.totalit.sbms.SyncData;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.HomeActivity;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.ClientRequestMapper;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.Notes;
import org.totalit.sbms.domain.ProcumentDocs;
import org.totalit.sbms.domain.VisitPlan;
import org.totalit.sbms.retrofit.BranchService;
import org.totalit.sbms.retrofit.ClientInventoryService;
import org.totalit.sbms.retrofit.ClientService;
import org.totalit.sbms.retrofit.ContactService;
import org.totalit.sbms.retrofit.NotesService;
import org.totalit.sbms.retrofit.ProcurementDocsservice;
import org.totalit.sbms.retrofit.RetrofitConfig;
import org.totalit.sbms.retrofit.ServiceGenerator;
import org.totalit.sbms.retrofit.VisitPlanService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SyncGetAll {
    AppDatabase mdb;

    int createdBy;

    List<Notes> notesList;
    List<VisitPlan> visitPlanList;
    Context context;
    CheckExistAll checkExistAll;

    Long companyId;
    String username;
    String password;
    SharedPreferences sharedPreferences;

    public SyncGetAll(AppDatabase mdb, Context context, int createdBy) {
        this.mdb = mdb;
        this.createdBy = createdBy;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        companyId = sharedPreferences.getLong(LoginActivity.companyIdPref, 0l);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");
        checkExistAll = new CheckExistAll(mdb);
    }

    public void getClientData(){

        ClientService clientService = ServiceGenerator.createService(ClientService.class, username, password);
        Call<ClientRequestMapper> mapperCall = clientService.getClientData(companyId, createdBy);
        mapperCall.enqueue(new Callback<ClientRequestMapper>() {
            @Override
            public void onResponse(Call<ClientRequestMapper> call, Response<ClientRequestMapper> response) {
                   if(response.isSuccessful()){
                       Log.e("Json", ServiceGenerator.gson.toJson(response.body()));

                       for (Client client : response.body().getClientList()){
                           if(!checkExistAll.checkClientByRealId(client.getRealId())) {
                               client.setSyncStatus(SyncStatus.SEND);
                               mdb.clientRepository().insertUser(client);
                           }
                       }
                       for(Contact contact : response.body().getContactList()){
                           if(!checkExistAll.checkContactByRealId(contact.getRealId())) {
                               contact.setSyncStatus(SyncStatus.SEND);
                               mdb.contactDao().insertContact(contact);
                           }
                       }
                       for(ProcumentDocs docs : response.body().getProcurementList()) {
                           if(!checkExistAll.checkProcDocsByRealId(docs.getRealId())) {
                               docs.setSyncStatus(SyncStatus.SEND);
                               mdb.procumentDocsDao().insertProcumentDoc(docs);
                           }
                       }
                       for (ClientInventory clientInventory : response.body().getInventoryList()){
                           if(!checkExistAll.checkClientInByRealId(clientInventory.getRealId())) {
                               clientInventory.setSyncStatus(SyncStatus.SEND);
                               mdb.clientInventoryDao().insertUser(clientInventory);
                           }
                       }
                   }
            }

            @Override
            public void onFailure(Call<ClientRequestMapper> call, Throwable t) {
                             Log.e("Get Client Err", t.getMessage()+"");
            }
        });
    }
/*
    public void getAllVisitPlansByCreatedBy(){
        visitPlanList = new ArrayList<>();
        Call<List<VisitPlan>> listCall = planService.getVisitPlanByCreatedBy(createdBy);
        listCall.enqueue(new Callback<List<VisitPlan>>() {
            @Override
            public void onResponse(Call<List<VisitPlan>> call, final Response<List<VisitPlan>> response) {
                if(response.isSuccessful()){

                            visitPlanList = response.body();
                            for(VisitPlan visitPlan : visitPlanList){
                                if(!checkExistAll.checkVisitPlanByRealId(visitPlan.getRealId())) {
                                    visitPlan.setSyncStatus(SyncStatus.SEND);
                                    mdb.visitPlanDao().insertVisitPlan(visitPlan);
                                }
                            }
                        }

            }

            @Override
            public void onFailure(Call<List<VisitPlan>> call, Throwable t) {
                Log.e("SyncGetAll/getVisitPlan", "onFailure  " + t.getMessage());
            }
        });
    }
    public void getNoteByCreatedBy(){
        notesList = new ArrayList<>();
        Call<List<Notes>> listCall = notesService.getNotesByCreatedBy(createdBy);
        listCall.enqueue(new Callback<List<Notes>>() {
            @Override
            public void onResponse(Call<List<Notes>> call, final Response<List<Notes>> response) {
                if(response.isSuccessful()){

                            notesList = response.body();
                            for (Notes notes : notesList){
                                if(!checkExistAll.checkNotesByRealId(notes.getRealId())) {
                                    notes.setSyncStatus(SyncStatus.SEND);
                                    mdb.notesDao().insertNote(notes);
                                }
                            }

                }
            }

            @Override
            public void onFailure(Call<List<Notes>> call, Throwable t) {
                Log.e("SyncGetAll/getNotes", "onFailure  " + t.getMessage());
            }
        });
    }
    */
}
