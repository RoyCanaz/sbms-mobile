package org.totalit.sbms.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.R;
import org.totalit.sbms.domain.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientProfileFragment extends Fragment {
    TextView nameOfClientProfile, address, description, email, phone, clientType, website;
    Button btnOpenClientProcDocs;
    int clientDbId;
    int uuid;
    String clientName;
    AppDatabase mdb;
    Client client;
    FloatingActionButton floatingActionButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_profile, container, false);

        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientDbId = this.getArguments().getInt("clientId");
        uuid = this.getArguments().getInt("uuid");
        clientName = this.getArguments().getString("clientName");
        nameOfClientProfile = view.findViewById(R.id.name_of_client_profile);
        description = view.findViewById(R.id.client_profile_descr);
        clientType = view.findViewById(R.id.client_profile_client_type);
        address = view.findViewById(R.id.client_profile_address);
        email = view.findViewById(R.id.client_profile_email);
        phone = view.findViewById(R.id.client_profile_phone);
        website = view.findViewById(R.id.client_profile_website);
        floatingActionButton = view.findViewById(R.id.fab_edit_profile);


        btnOpenClientProcDocs = view.findViewById(R.id.btn_open_proc_docs);
        btnOpenClientProcDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateProDocs(v);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateEditProfile(v);
            }
        });

        setClientDetails();
        return view;
    }
    private void setClientDetails(){
        try {
             client = new LoadClient(mdb, clientDbId, uuid).execute().get();
             nameOfClientProfile.setText(client.getName());
             clientType.setText(client.getClientType());
             description.setText(client.getDescription());
             address.setText(client.getAddress());
             email.setText(client.getEmail());
             website.setText(client.getWebsite());
             phone.setText(client.getPhone());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    private void navigateProDocs(View view){
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        fragment = new ClientProcurementDocsFragment();
        bundle.putInt("clientId", clientDbId);
        bundle.putInt("uuid", uuid);
        bundle.putString("clientName", clientName);
        fragment.setArguments(bundle);

        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
    private void navigateEditProfile(View view){
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        fragment = new FragmentEditClientDetails();
        bundle.putInt("clientId", clientDbId);
        bundle.putInt("uuid", uuid);
        bundle.putString("clientName", clientName);
        fragment.setArguments(bundle);

        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }


    public  class LoadClient extends AsyncTask<Void, Void, Client> {

        AppDatabase mdb;
        int id;
        int uuid;

        public LoadClient(AppDatabase mdb, int id, int uuid) {
            this.id = id;
            this.mdb = mdb;
            this.uuid = uuid;
        }

        @Override
        protected Client doInBackground(Void... voids) {
/*
            Client client = mdb.clientRepository().findOneById(id);
            if(client!=null){
                return  client;
            }

                Client clientRealId = mdb.clientRepository().findOneByRealId(id);
                return clientRealId;
        }
*/

            Client client = mdb.clientRepository().findOneByIdAndUuid(id, uuid);
            return client;
        }
    }
}
