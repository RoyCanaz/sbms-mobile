package org.totalit.sbms.Sync;

import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.HomeActivity;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.SendAndReceive.ClientData;
import org.totalit.sbms.SyncData.CheckExistAll;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.adapters.ClientAdapter;
import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.ClientRequestMapper;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.Notes;
import org.totalit.sbms.domain.ProcumentDocs;
import org.totalit.sbms.domain.VisitPlan;
import org.totalit.sbms.fragments.ClientManagementHomeFragment;
import org.totalit.sbms.retrofit.ClientService;
import org.totalit.sbms.retrofit.ServiceGenerator;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PullService {
    ExecutorService executorService;
    AppDatabase mdb;
    Long companyId;
    String username;
    String password;
    int userid;

    SharedPreferences sharedPreferences;
  //  CheckExistAll checkExistAll;
    Context context;
    private final WeakReference<ProgressDialog> progressDialogWeakReference;
    Integer count;

    public PullService(AppDatabase mdb, Context context,  ProgressDialog progressDialog) {
        this.mdb = mdb;
        this.context = context;
        this.progressDialogWeakReference = new WeakReference<>(progressDialog);
        sharedPreferences = context.getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        companyId = sharedPreferences.getLong(LoginActivity.companyIdPref, 0l);
        username = sharedPreferences.getString(LoginActivity.usernamePref, "");
        password = sharedPreferences.getString(LoginActivity.passwordPref, "");
        userid = sharedPreferences.getInt(LoginActivity.idPref, 0);
       // executorService = Executors.newSingleThreadExecutor();
       // checkExistAll = new CheckExistAll(mdb);

    }
    UpdateClientDataId updateClientDataId = null;
   public void sendClientData() throws ExecutionException, InterruptedException {
       List<Client> clients = new ClientData(mdb).execute().get();
       count = clients.size();
       final ProgressDialog dialog = progressDialogWeakReference.get();
       dialog.setTitle("Sync");
       dialog.setMessage("Loading Please wait...");
       dialog.show();

       for (final Client client : clients) {
            client.setCompanyId(companyId);
           final ProgressDialog progressDialog = progressDialogWeakReference.get();
            ClientDataMapper clientDataMapper = new MapClientData(mdb, context, progressDialog, client, companyId).execute().get();
           ClientService clientService = ServiceGenerator.createService(ClientService.class, username, password);
           Call<ClientDataMapper> clientDataMapperCall = clientService.saveClient(clientDataMapper, companyId);
           clientDataMapperCall.enqueue(new Callback<ClientDataMapper>() {
               @Override
               public void onResponse(Call<ClientDataMapper> call, Response<ClientDataMapper> response) {
                   if(response.isSuccessful()) {
                       Log.e("Json", ServiceGenerator.gson.toJson(response.body()));
                       updateClientDataId = new UpdateClientDataId(mdb,context, progressDialog,  response.body(), companyId, username, password, userid);
                       updateClientDataId.execute();
                    /*   Log.e("Count", count + "");
                       count --;
                       Log.e("Count1", count + "");
                       if(count == 0){
                           ClientDataNotSynced notSynced = new ClientDataNotSynced(mdb, context, progressDialog, companyId, username, password, userid);
                           notSynced.execute();
                       }
                       /*try {
                           List<Client> clients = new ClientData(mdb).execute().get();
                           if(clients.isEmpty()){
                               ClientDataNotSynced notSynced = new ClientDataNotSynced(mdb, context, progressDialog, companyId, username, password, userid);
                               notSynced.execute();
                           }

                       } catch (InterruptedException | ExecutionException e) {
                           e.printStackTrace();
                       }*/

                   }
               }

               @Override
               public void onFailure(Call<ClientDataMapper> call, Throwable t) {
                   Log.e("Sync Err", t.getMessage() + "");
                   if(progressDialogWeakReference.get()!=null && progressDialogWeakReference.get().isShowing()){
                       progressDialogWeakReference.get().dismiss();
                   }
               }
           });

       }

       Log.e("Company Id", companyId+"");
      //Retrieving Client Data
       ClientService clientService = ServiceGenerator.createService(ClientService.class, username, password);
       Call<ClientRequestMapper> mapperCall = clientService.getClientData(companyId, userid);
       mapperCall.enqueue(new Callback<ClientRequestMapper>() {
           @Override
           public void onResponse(Call<ClientRequestMapper> call, Response<ClientRequestMapper> response) {
               if(response.isSuccessful()){
                   Log.e("Client Data..", ServiceGenerator.gson.toJson(response.body()));
                    GetAll getAll = new GetAll(mdb, context, dialog,response.body(), companyId, username, password, userid);
                    getAll.execute();
               }
           }

           @Override
           public void onFailure(Call<ClientRequestMapper> call, Throwable t) {
               Log.e("Get Client Err", t.getMessage()+"");
               if(progressDialogWeakReference.get()!=null && progressDialogWeakReference.get().isShowing()){
                   progressDialogWeakReference.get().dismiss();
               }
               Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
           }
       });

   }
    private class MapClientData extends AsyncTask<Void, Void, ClientDataMapper> {
        AppDatabase mdb;
        Client client;
        Long companyId;
        Context context;
        private final WeakReference<ProgressDialog> progressDialogWeakReference;

        public MapClientData(AppDatabase mdb, Context context,  ProgressDialog progressDialog, Client client, Long companyId) {
            this.mdb = mdb;
            this.client = client;
            this.companyId = companyId;
            this.context = context;
            this.progressDialogWeakReference = new WeakReference<>(progressDialog);
        }


        @Override
        protected ClientDataMapper doInBackground(Void... voids) {
            Log.e("Sync", "Snycing New Client Data");
            ClientDataMapper clientDataMapper = new ClientDataMapper();
            clientDataMapper.setClient(client);
            clientDataMapper.setContactList(mdb.contactDao().getAllNotSynced(client.getId()));
            clientDataMapper.setClientInventoryList(mdb.clientInventoryDao().getAllNotSynced(client.getId()));
            clientDataMapper.setDocument(mdb.procumentDocsDao().getByNotSynced(client.getId()));
            clientDataMapper.setBranchList(mdb.branchDao().getAllNotSynced(client.getId()));
            clientDataMapper.setVisitPlanList(mdb.visitPlanDao().getAllNotSynced(client.getId()));
            clientDataMapper.setNoteList(mdb.notesDao().getAllNotSynced(client.getId()));
            return clientDataMapper;
        }



    }


   private class UpdateClientDataId extends AsyncTask<Void, Void, Void>{
       AppDatabase mdb;
       Long companyId;
       ClientDataMapper clientDataMapper;
       String username;
       String password;
       int userid;
       Context context;
       private final WeakReference<ProgressDialog> progressDialogWeakReference;

       public UpdateClientDataId(AppDatabase mdb, Context context,  ProgressDialog progressDialog, ClientDataMapper clientDataMapper, Long companyId, String username, String password, int userid) {

           this.mdb = mdb;
           this.companyId = companyId;
           this.clientDataMapper = clientDataMapper;
           this.username = username;
           this.password = password;
           this.userid = userid;
           this.context = context;
           this.progressDialogWeakReference = new WeakReference<>(progressDialog);
       }


       @Override
       protected Void doInBackground(Void... voids) {
           Log.e("Run Update", "Updatting Client Data Id");
           Client mapClient = clientDataMapper.getClient();
           Client client = mdb.clientRepository().findOneById(mapClient.getId());
           client.setRealId(mapClient.getRealId());
           client.setSyncStatus(SyncStatus.SEND);
           mdb.clientRepository().updateClient(client);

           ProcumentDocs docs = clientDataMapper.getDocument();
           ProcumentDocs procumentDocs = mdb.procumentDocsDao().findOneById(docs.getId());
           procumentDocs.setRealId(docs.getRealId());
           procumentDocs.setSyncStatus(SyncStatus.SEND);
           procumentDocs.setClientId(docs.getClientId());
           mdb.procumentDocsDao().updateProcurement(procumentDocs);


           for (Contact contact : clientDataMapper.getContactList()){
               Contact con = mdb.contactDao().findOneById(contact.getId());
               con.setSyncStatus(SyncStatus.SEND);
               con.setRealId(contact.getRealId());
               con.setClientId(mapClient.getRealId());
               mdb.contactDao().updateContact(con);

           }
           for(Branch branch : clientDataMapper.getBranchList()){
               Branch br = mdb.branchDao().findOneById(branch.getId());
               br.setSyncStatus(SyncStatus.SEND);
               br.setRealId(branch.getRealId());
               br.setClientId(mapClient.getRealId());
               mdb.branchDao().updateBranch(br);
           }
           for(ClientInventory clientInventory : clientDataMapper.getClientInventoryList()){
               ClientInventory inventory = mdb.clientInventoryDao().findOneById(clientInventory.getId());
               inventory.setSyncStatus(SyncStatus.SEND);
               inventory.setRealId(clientInventory.getRealId());
               inventory.setClientId(mapClient.getRealId());
               mdb.clientInventoryDao().update(inventory);
           }
           for(Notes note : clientDataMapper.getNoteList()){
               Notes not = mdb.notesDao().findOneById(note.getId());
               not.setSyncStatus(SyncStatus.SEND);
               not.setRealId(note.getRealId());
               not.setClientId(mapClient.getRealId());
               mdb.notesDao().updateNote(not);
           }
           for(VisitPlan visitPlan : clientDataMapper.getVisitPlanList()){
               VisitPlan plan = mdb.visitPlanDao().findOneById(visitPlan.getId());
               plan.setSyncStatus(SyncStatus.SEND);
               plan.setRealId(visitPlan.getRealId());
               plan.setClientId(mapClient.getRealId());
               mdb.visitPlanDao().updateVisitPlan(plan);
           }
           return null;
       }

   }

    private class GetAll extends AsyncTask<Void, Void, Void> {
        AppDatabase mdb;
        Long companyId;
        ClientRequestMapper clientDataMapper;
        String username;
        String password;
        int userid;
        Context context;
        private final WeakReference<ProgressDialog> progressDialogWeakReference;
        CheckExistAll checkExistAll;

        public GetAll(AppDatabase mdb, Context context, ProgressDialog progressDialog, ClientRequestMapper clientDataMapper, Long companyId, String username, String password, int userid) {
            this.mdb = mdb;
            this.companyId = companyId;
            this.clientDataMapper = clientDataMapper;
            this.username = username;
            this.password = password;
            this.userid = userid;
            this.context = context;
            this.progressDialogWeakReference = new WeakReference<>(progressDialog);
            checkExistAll = new CheckExistAll(mdb);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (Client client : clientDataMapper.getClientList()){
                if(!checkExistAll.checkClientByRealId(client.getRealId())) {
                    client.setSyncStatus(SyncStatus.SEND);
                    mdb.clientRepository().insertUser(client);
                }
            }
            for(Contact contact : clientDataMapper.getContactList()){
                if(!checkExistAll.checkContactByRealId(contact.getRealId())) {
                    contact.setSyncStatus(SyncStatus.SEND);
                    mdb.contactDao().insertContact(contact);
                }
            }
            for(ProcumentDocs docs : clientDataMapper.getProcurementList()) {
                if(!checkExistAll.checkProcDocsByRealId(docs.getRealId())) {
                    docs.setSyncStatus(SyncStatus.SEND);
                    mdb.procumentDocsDao().insertProcumentDoc(docs);
                }
            }
            for (ClientInventory clientInventory : clientDataMapper.getInventoryList()){
                if(!checkExistAll.checkClientInByRealId(clientInventory.getRealId())) {
                    clientInventory.setSyncStatus(SyncStatus.SEND);
                    mdb.clientInventoryDao().insertUser(clientInventory);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ProgressDialog dialog = progressDialogWeakReference.get();
            ClientDataNotSynced notSynced = new ClientDataNotSynced(mdb, context, dialog, companyId, username, password, userid);
            notSynced.execute();
        }
    }
    private class ClientDataNotSynced extends AsyncTask<Void, Void, ClientDataMapperX> {
        AppDatabase mdb;
        Long companyId;
        // ClientDataMapperX clientDataMapper;
        String username;
        String password;
        int userid;
        Context context;
        private final WeakReference<ProgressDialog> progressDialogWeakReference;

        public ClientDataNotSynced(AppDatabase mdb, Context context,  ProgressDialog progressDialog, Long companyId, String username, String password, int userid) {
            this.mdb = mdb;
            this.companyId = companyId;
            // this.clientDataMapper = clientDataMapper;
            this.username = username;
            this.password = password;
            this.userid = userid;
            this.context = context;
            this.progressDialogWeakReference = new WeakReference<>(progressDialog);
        }


        @Override
        protected ClientDataMapperX doInBackground(Void... voids) {
            Log.e("Sync", "Getting Not Synced Data...");
            ClientDataMapperX clientDataMapper = new ClientDataMapperX();
            clientDataMapper.setContactList(mdb.contactDao().getAllNotSynced());
            clientDataMapper.setClientInventoryList(mdb.clientInventoryDao().getAllNotSynced());
            clientDataMapper.setBranchList(mdb.branchDao().getAllNotSynced());
            clientDataMapper.setVisitPlanList(mdb.visitPlanDao().getAllNotSynced());
            clientDataMapper.setNoteList(mdb.notesDao().getAllNotSynced());
            return clientDataMapper;
        }

        @Override
        protected void onPostExecute(final ClientDataMapperX mapperX) {
            super.onPostExecute(mapperX);
            final ProgressDialog progressDialog = progressDialogWeakReference.get();
            Log.e("mapperX json", ServiceGenerator.gson.toJson(mapperX));

            ClientService clientService = ServiceGenerator.createService(ClientService.class, username, password);
            Call<ClientDataMapperX> mapperXCall = clientService.clientDatax(mapperX, companyId);
            mapperXCall.enqueue(new Callback<ClientDataMapperX>() {
                @Override
                public void onResponse(Call<ClientDataMapperX> call, Response<ClientDataMapperX> response) {
                    if(response.isSuccessful()){
                        Log.e("DataX Res J", ServiceGenerator.gson.toJson(response.body()));
                        MapClientDataX x = new MapClientDataX(mdb, context, progressDialog, response.body(), companyId, username, password, userid);
                        x.execute();
                    }
                }

                @Override
                public void onFailure(Call<ClientDataMapperX> call, Throwable t) {
                        //  Log.e("Error X", ""+t.getMessage());
                    if(progressDialogWeakReference.get()!=null && progressDialogWeakReference.get().isShowing()){
                        progressDialogWeakReference.get().dismiss();
                    }
                    Toast.makeText(context, "Error Encounted", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    private class MapClientDataX extends AsyncTask<Void, Void, Void> {
        AppDatabase mdb;
        Long companyId;
        ClientDataMapperX clientDataMapper;
        String username;
        String password;
        int userid;
        Context context;
        private final WeakReference<ProgressDialog> progressDialogWeakReference;

        public MapClientDataX(AppDatabase mdb, Context context,  ProgressDialog progressDialog, ClientDataMapperX clientDataMapper, Long companyId, String username, String password, int userid) {
            this.mdb = mdb;
            this.companyId = companyId;
            this.clientDataMapper = clientDataMapper;
            this.username = username;
            this.password = password;
            this.userid = userid;
            this.context = context;
            this.progressDialogWeakReference = new WeakReference<>(progressDialog);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for (Contact contact : clientDataMapper.getContactList()){
                Contact con = mdb.contactDao().findOneById(contact.getId());
                con.setSyncStatus(SyncStatus.SEND);
                con.setRealId(contact.getRealId());
                mdb.contactDao().updateContact(con);
            }
            for(Branch branch : clientDataMapper.getBranchList()){
                Branch br = mdb.branchDao().findOneById(branch.getId());
                br.setSyncStatus(SyncStatus.SEND);
                br.setRealId(branch.getRealId());
                mdb.branchDao().updateBranch(br);
            }
            for(ClientInventory clientInventory : clientDataMapper.getClientInventoryList()){
                ClientInventory inventory = mdb.clientInventoryDao().findOneById(clientInventory.getId());
                inventory.setSyncStatus(SyncStatus.SEND);
                inventory.setRealId(clientInventory.getRealId());
                mdb.clientInventoryDao().update(inventory);
            }
            for(Notes note : clientDataMapper.getNoteList()){
                Notes not = mdb.notesDao().findOneById(note.getId());
                not.setSyncStatus(SyncStatus.SEND);
                not.setRealId(note.getRealId());

                mdb.notesDao().updateNote(not);
            }
            for(VisitPlan visitPlan : clientDataMapper.getVisitPlanList()){
                VisitPlan plan = mdb.visitPlanDao().findOneById(visitPlan.getId());
                plan.setSyncStatus(SyncStatus.SEND);
                plan.setRealId(visitPlan.getRealId());
                mdb.visitPlanDao().updateVisitPlan(plan);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ProgressDialog progressDialog = progressDialogWeakReference.get();

            if(progressDialogWeakReference.get()!=null && progressDialogWeakReference.get().isShowing()){
                progressDialogWeakReference.get().dismiss();
            }
            HomeActivity homeActivity = (HomeActivity)context;
             homeActivity.clientMana();


            Toast.makeText(context, "Sync Successfull", Toast.LENGTH_SHORT).show();
        }
    }


}
