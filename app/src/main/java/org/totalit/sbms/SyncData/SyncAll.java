package org.totalit.sbms.SyncData;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.R;
import org.totalit.sbms.SendAndReceive.BranchData;
import org.totalit.sbms.SendAndReceive.ClientInventoryData;
import org.totalit.sbms.SendAndReceive.ContactsData;
import org.totalit.sbms.SendAndReceive.NotesData;
import org.totalit.sbms.SendAndReceive.ProcurementDocsData;
import org.totalit.sbms.SendAndReceive.VisitPlanData;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.Notes;
import org.totalit.sbms.domain.ProcumentDocs;
import org.totalit.sbms.domain.VisitPlan;
import org.totalit.sbms.retrofit.BranchService;
import org.totalit.sbms.retrofit.CategoryService;
import org.totalit.sbms.retrofit.ClientInventoryService;
import org.totalit.sbms.retrofit.ClientService;
import org.totalit.sbms.retrofit.ContactService;
import org.totalit.sbms.retrofit.NotesService;
import org.totalit.sbms.retrofit.ProcurementDocsservice;
import org.totalit.sbms.retrofit.RetrofitConfig;
import org.totalit.sbms.retrofit.UserService;
import org.totalit.sbms.retrofit.VisitPlanService;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SyncAll  {
    AppDatabase mdb;
    ClientInventoryService clientInventoryService;
    ProcurementDocsservice docsservice;
    ContactService contactService;
    BranchService branchService;
    NotesService notesService;
    VisitPlanService planService;
    Retrofit retrofit;
    UpdateFromResponse updateFromResponse;

    public SyncAll(AppDatabase mdb) {
        this.mdb = mdb;
        retrofit = RetrofitConfig.retrofitConfig();
        clientInventoryService = retrofit.create(ClientInventoryService.class);
        contactService = retrofit.create(ContactService.class);
        branchService = retrofit.create(BranchService.class);
        docsservice = retrofit.create(ProcurementDocsservice.class);
        notesService = retrofit.create(NotesService.class);
        planService = retrofit.create(VisitPlanService.class);
        updateFromResponse = new UpdateFromResponse(mdb);

    }
    public void syncBranches() throws ExecutionException, InterruptedException {
        List<Branch> list = new BranchData.BranchByNotSynced(mdb).execute().get();

        for (final Branch branch : list){
            Call<Branch> branchCall = branchService.sendBranch(branch);;
            branchCall.enqueue(new Callback<Branch>() {
                @Override
                public void onResponse(Call<Branch> call, Response<Branch> response) {
                    if(response.isSuccessful()){

                                Branch b = new Branch();
                                b.setClientId(branch.getId());
                                b.setSyncStatus(1);
                                b.setRealId(response.body().getRealId());
                                b.setBranch(response.body().getBranch());
                                b.setDescription(response.body().getDescription());
                                b.setAddress(response.body().getAddress());
                                b.setCreatedBy(response.body().getCreatedBy());
                                updateFromResponse.updateBranch(b);
                            }

                }

                @Override
                public void onFailure(Call<Branch> call, Throwable t) {
                    Log.d("LoginActivity", "onFailure" + t.getMessage());
                }
            });
        }
    }
    public void syncContacts() throws ExecutionException, InterruptedException {
        List<Contact> contactList = new ContactsData(mdb).execute().get();
        for(final Contact contact : contactList){
            Call<Contact> contactCall = contactService.sendContacts(contact);
            contactCall.enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    if(response.isSuccessful()){
                               Contact con = new Contact();
                                con.setId(contact.getId());
                                con.setClientId(response.body().getClientId());
                                con.setSyncStatus(SyncStatus.SEND);
                                con.setRealId(response.body().getRealId());
                                con.setFirstName(response.body().getFirstName());
                                con.setLastName(response.body().getLastName());
                                con.setEmail(response.body().getEmail());
                                con.setDepartment(response.body().getDepartment());
                                con.setJobPosition(response.body().getJobPosition());
                                con.setMobilePhone(response.body().getMobilePhone());
                                con.setOfficePhone(response.body().getOfficePhone());
                                con.setCreatedBy(response.body().getCreatedBy());
                                updateFromResponse.updateContact(con);
                            }
                }

                @Override
                public void onFailure(Call<Contact> call, Throwable t) {
                    Log.d("LoginActivity", "onFailure" + t.getMessage());
                }
            });
        }
    }

    public void syncProcurementDocs() throws ExecutionException, InterruptedException {
        List<ProcumentDocs> procumentDocs = new ProcurementDocsData(mdb).execute().get();
        for( final ProcumentDocs documents : procumentDocs){
            Call<ProcumentDocs> procumentDocsCall = docsservice.sendProcurement(documents);
            procumentDocsCall.enqueue(new Callback<ProcumentDocs>() {
                @Override
                public void onResponse(Call<ProcumentDocs> call,  Response<ProcumentDocs> response) {
                    if(response.isSuccessful()){

                                ProcumentDocs docs = new ProcumentDocs();
                                docs.setId(documents.getId());
                                docs.setClientId(response.body().getClientId());
                                docs.setSyncStatus(SyncStatus.SEND);
                                docs.setRealId(response.body().getRealId());
                                docs.setApplicationLetter(response.body().getApplicationLetter());
                                docs.setCrFourteen(response.body().getCrFourteen());
                                docs.setOther(response.body().getOther());
                                docs.setTraceableReference(response.body().getTraceableReference());
                                docs.setTradeLicense(response.body().getTradeLicense());
                                docs.setItf(response.body().getItf());
                                docs.setVat(response.body().getVat());
                                docs.setMou(response.body().getMou());
                                docs.setCertOfIncorporation(response.body().getCertOfIncorporation());
                                docs.setCompanyProfile(response.body().getCompanyProfile());
                                docs.setCreatedBy(response.body().getCreatedBy());
                                updateFromResponse.updateProcDocs(docs);
                            }
                }

                @Override
                public void onFailure(Call<ProcumentDocs> call, Throwable t) {
                    Log.d("LoginActivity", "onFailure" + t.getMessage());
                }
            });
        }
    }

    public void syncClientInventory() throws ExecutionException, InterruptedException {
        List<ClientInventory> inventories = new ClientInventoryData(mdb).execute().get();
        for (final ClientInventory ci : inventories){
            Call<ClientInventory> clientInventoryCall = clientInventoryService.sendClientInventory(ci);
            clientInventoryCall.enqueue(new Callback<ClientInventory>() {
                @Override
                public void onResponse(Call<ClientInventory> call, retrofit2.Response<ClientInventory> response) {
                    if(response.isSuccessful()){
                                ClientInventory clientInventory = new ClientInventory();
                                clientInventory.setId(ci.getId());
                                clientInventory.setClientId(response.body().getClientId());
                                clientInventory.setSyncStatus(1);
                                clientInventory.setRealId(response.body().getRealId());
                                clientInventory.setCategory(response.body().getCategory());
                                clientInventory.setModel(response.body().getModel());
                                clientInventory.setTonerType(response.body().getTonerType());
                                clientInventory.setDescription(response.body().getDescription());
                                clientInventory.setQuantity(response.body().getQuantity());
                                clientInventory.setNeedMaintenence(response.body().getNeedMaintenence());
                                clientInventory.setClientId(response.body().getClientId());
                                clientInventory.setCreatedBy(response.body().getCreatedBy());
                                updateFromResponse.updateClientIn(clientInventory);

                            }

                }

                @Override
                public void onFailure(Call<ClientInventory> call, Throwable t) {
                    Log.d("LoginActivity", "onFailure" + t.getMessage());

                }
            });
        }
    }
    public void syncNotes() throws ExecutionException, InterruptedException {
         List<Notes> notesList = new NotesData(mdb).execute().get();
         for(final Notes notes : notesList){
            Call<Notes> notesCall = notesService.sendNotes(notes);
            notesCall.enqueue(new Callback<Notes>() {
                @Override
                public void onResponse(Call<Notes> call, Response<Notes> response) {
                    if(response.isSuccessful()){

                                Notes note = new Notes();
                                note.setId(notes.getId());
                                note.setClientId(response.body().getClientId());
                                note.setSyncStatus(SyncStatus.SEND);
                                note.setNote(response.body().getNote());
                                note.setRealId(response.body().getRealId());
                                updateFromResponse.updateNotes(note);
                            }

                }

                @Override
                public void onFailure(Call<Notes> call, Throwable t) {

                }
            });
         }
    }
    public void syncVisitPlan() throws ExecutionException, InterruptedException {
        List<VisitPlan> visitPlans = new VisitPlanData(mdb).execute().get();
        for (final VisitPlan plan : visitPlans){
            Call<VisitPlan> planCall = planService.sendVisitPlan(plan);
            planCall.enqueue(new Callback<VisitPlan>() {
                @Override
                public void onResponse(Call<VisitPlan> call, Response<VisitPlan> response) {
                    if(response.isSuccessful()){
                        VisitPlan visitPlan = new VisitPlan();
                        visitPlan.setId(plan.getId());
                        visitPlan.setClientId(response.body().getClientId());
                        visitPlan.setRealId(response.body().getRealId());
                        visitPlan.setSyncStatus(SyncStatus.SEND);
                        visitPlan.setStatus(response.body().getStatus());
                        visitPlan.setDateOfVisit(response.body().getDateOfVisit());
                        visitPlan.setVisitResult(response.body().getVisitResult());
                        visitPlan.setCreatedBy(response.body().getCreatedBy());
                        updateFromResponse.updateVisit(visitPlan);
                    }
                }

                @Override
                public void onFailure(Call<VisitPlan> call, Throwable t) {

                }
            });

        }
    }
}
