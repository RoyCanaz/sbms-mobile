package org.totalit.sbms.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.ProcumentDocs;

public class ProcumentDocsFragment extends Fragment {

    private CheckBox appLetter, companyProfile, certOfinc, mou, crFouthten, vatReg, itfCert, tradeLicense, tracebleRef;
    private Button saveDocuments;

    String appL;
    String comProf;
    String certOf;
    String memoOf;
    String crFour;
    String vatR;
    String itfCertificate;
    String tradeLi;
    String traceR;
    private EditText otherDocs;
    AppDatabase mdb;
    int clientId;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_procurement_documents,container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        otherDocs = view.findViewById(R.id.input_other);
        saveDocuments = view.findViewById(R.id.saveDocuments);
        clientId = this.getArguments().getInt("clientId");
        checkBoxListerner(view);
        saveDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRequiredDocs(v);
                navigateToAddInventory(v);
            }
        });

        return view;
    }

    private void checkBoxListerner(View view){
        appLetter = view.findViewById(R.id.app_letter);
        companyProfile = view.findViewById(R.id.com_profile);
        certOfinc = view.findViewById(R.id.cert_of_inc);
        mou = view.findViewById(R.id.mou);
        crFouthten = view.findViewById(R.id.cr14);
        vatReg = view.findViewById(R.id.vat);
        itfCert = view.findViewById(R.id.itf);
        tradeLicense = view.findViewById(R.id.trade_license);
        tracebleRef = view.findViewById(R.id.trace_ref);

    }
    private void saveRequiredDocs(View v){
        appL = appLetter.isChecked() ? "Yes" : "No";
        comProf = companyProfile.isChecked() ? "Yes" : "No";
        certOf = certOfinc.isChecked() ?  "Yes" : "No";
        memoOf = mou.isChecked() ? "Yes" : "No";
        crFour = crFouthten.isChecked() ? "Yes" : "No";
        vatR = vatReg.isChecked() ? "Yes" : "No";
        itfCertificate = itfCert.isChecked() ? "Yes" : "No";
        tradeLi =  tradeLicense.isChecked() ? "Yes" : "No";
        traceR = tracebleRef.isChecked() ? "Yes" : "No";

        final ProcumentDocs p = new ProcumentDocs();
        p.setRealId(0);
        p.setSyncStatus(SyncStatus.NOT_SEND);
        p.setApplicationLetter(appL);
        p.setCompanyProfile(comProf);
        p.setCertOfIncorporation(certOf);
        p.setMou(memoOf);
        p.setCrFourteen(crFour);
        p.setVat(vatR);
        p.setItf(itfCertificate);
        p.setTradeLicense(tradeLi);
        p.setTraceableReference(traceR);
        p.setOther(otherDocs.getText().toString());
        p.setClientId(clientId);
        p.setCreatedBy(sharedPreferences.getInt(LoginActivity.idPref, 1));
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.procumentDocsDao().insertProcumentDoc(p);

            }
        });
        Toast.makeText(getActivity().getApplicationContext(), "Procurement Documents Saved successfully", Toast.LENGTH_LONG).show();
    }
    private void navigateToAddInventory(View view){
        Fragment fragment = null;
        Bundle bundle = new Bundle();
      //  fragment = new ClientInventoryFragment();
        fragment = new AddContactFragment();
        bundle.putInt("clientId", clientId);
        fragment.setArguments(bundle);

        if (fragment != null) {
            saveDocuments.setEnabled(false);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();

        }
    }
}
