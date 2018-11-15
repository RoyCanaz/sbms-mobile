package org.totalit.sbms.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.R;
import org.totalit.sbms.adapters.ContactsAdapter;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientContactsFragment extends Fragment {
    TextView nameOfClientProfile;
    FloatingActionButton addContact;
    int clientDbId;
    int uuid;
    String clientName;
    AppDatabase mdb;
    RecyclerView recyclerView;
    FloatingActionButton addContactFab;
    //RecyclerView.Adapter adapter;
    ContactsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<Contact> contacts;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_contacts, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientDbId = this.getArguments().getInt("clientId");
        uuid = this.getArguments().getInt("uuid");
        clientName = this.getArguments().getString("clientName");
        addContactFab = view.findViewById(R.id.fab_new_contact);
        nameOfClientProfile = view.findViewById(R.id.name_of_client_contacts);
        addContact = view.findViewById(R.id.fab_new_contact);
        nameOfClientProfile.setText(clientName);

        try {
            contacts = new LoadClientContacts(mdb, clientDbId, uuid).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        recyclerView = view.findViewById(R.id.recycler_view_contacts);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ContactsAdapter(contacts);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        addContactFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                fragment = new NewContactFragment();
                bundle.putInt("clientId", clientDbId);
                bundle.putInt("uuid", uuid);
                bundle.putString("clientName", clientName);
                fragment.setArguments(bundle);
                openFragment(fragment);
            }
        });
        if(contacts.size()==0){
            Toast.makeText(getActivity().getApplicationContext(), "No Contacts Found", Toast.LENGTH_LONG).show();
        }
        return  view;
    }

    private void openFragment(Fragment fragment){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();

    }

    public class LoadClientContacts extends AsyncTask<Void, Void, List<Contact>> {

        AppDatabase mdb;
        int id;
        int uuid;

        public LoadClientContacts(AppDatabase mdb, int id, int uuid) {
            this.id = id;
            this.mdb = mdb;
            this.uuid = uuid;
        }

        @Override
        protected List<Contact> doInBackground(Void... voids) {
            if(uuid==0) {
                List<Contact> contacts = mdb.contactDao().findByClientId(id);
                return contacts;
            }
            List<Contact> contacts = mdb.contactDao().findByClientId(uuid);
            return contacts;
        }


    }
}
