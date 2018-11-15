package org.totalit.sbms.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.ProcumentDocs;

import java.util.concurrent.ExecutionException;

public class ClientProcurementDocsFragment extends Fragment {

    TextView nameOfClientProfile, appLetter, companyProfile, certOfinc, mou, crFouthten, vatReg, itfCert, tradeLicense, tracebleRef, other;
    int clientDbId;
    int uuid;
    String clientName;
    AppDatabase mdb;
    Client client;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_procurement_docs, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientDbId = this.getArguments().getInt("clientId");
        uuid = this.getArguments().getInt("uuid");
        clientName = this.getArguments().getString("clientName");


        nameOfClientProfile = view.findViewById(R.id.name_of_client_procurement);
        appLetter = view.findViewById(R.id.client_proc_app_letter);
        companyProfile = view.findViewById(R.id.client_proc_com_profile);
        certOfinc = view.findViewById(R.id.client_proc_cert_of_inc);
        mou = view.findViewById(R.id.client_proc_mou);
        crFouthten = view.findViewById(R.id.client_proc_cr14);
        vatReg = view.findViewById(R.id.client_proc_vat);
        itfCert = view.findViewById(R.id.client_proc_itf);
        tradeLicense = view.findViewById(R.id.client_proc_trade_license);
        tracebleRef = view.findViewById(R.id.client_proc_trace_ref);
        other = view.findViewById(R.id.client_proc_other);
        nameOfClientProfile.setText(clientName);

            setProcValues();

        return  view;
    }
    private void setProcValues() {
        ProcumentDocs p = null;
        try {
            p = new LoadClientProcurement(mdb, clientDbId, uuid).execute().get();
            if(p==null){
                return;
            }
            else {
                appLetter.setText(p.getApplicationLetter() == null ? "No" : p.getApplicationLetter());
                companyProfile.setText(p.getCompanyProfile() == null ? "No" : p.getCompanyProfile());
                certOfinc.setText(p.getCertOfIncorporation() == null ? "No" : p.getCertOfIncorporation());
                mou.setText(p.getMou() == null ? "No" : p.getMou());
                crFouthten.setText(p.getCrFourteen() == null ? "No" : p.getCrFourteen());
                vatReg.setText(p.getVat() == null ? "No" : p.getVat());
                itfCert.setText(p.getItf() == null ? "No" : p.getItf());
                tradeLicense.setText(p.getTradeLicense() == null ? "No" : p.getTradeLicense());
                tracebleRef.setText(p.getTraceableReference() == null ? "No" : p.getTraceableReference());
                other.setText(p.getOther() == null ? "No" : p.getOther());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    public class LoadClientProcurement extends AsyncTask<Void, Void, ProcumentDocs> {

        AppDatabase mdb;
        int id;
        int uuid;

        public LoadClientProcurement(AppDatabase mdb, int id, int uuid) {
            this.id = id;
            this.mdb = mdb;
            this.uuid = uuid;
        }

        @Override
        protected ProcumentDocs doInBackground(Void... voids) {
            if(uuid==0){
                ProcumentDocs procumentDocs = mdb.procumentDocsDao().findByClientId(id);
                return procumentDocs;
            }
            ProcumentDocs procumentDocs = mdb.procumentDocsDao().findByClientId(uuid);
            return procumentDocs;
        }
    }
}
