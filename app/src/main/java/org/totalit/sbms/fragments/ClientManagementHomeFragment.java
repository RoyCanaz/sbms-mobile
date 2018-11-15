package org.totalit.sbms.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.totalit.sbms.ClassImplements.EditTextSearchField;
import org.totalit.sbms.Database.AbstractDb;
import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.Database.InstantiateDb;
import org.totalit.sbms.R;
import org.totalit.sbms.adapters.ClientAdapter;
import org.totalit.sbms.domain.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

public class ClientManagementHomeFragment extends Fragment{
     RecyclerView recyclerView;
   //  RecyclerView.Adapter adapter;
     RecyclerView.LayoutManager layoutManager;
    ClientAdapter adapter;
     List<Client> clients;
    List<Client> nilClient;
    FloatingActionButton clientMan;
    EditText searchtextview;
    AppDatabase mdb;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.client_management_fragment, container, false);
       // container.clearDisappearingChildren();
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientMan = v.findViewById(R.id.clientMan);
        searchtextview = v.findViewById(R.id.search_text_view);


        try {
            clients = new LoadClientList(mdb).execute().get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(clients!=null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            recyclerView = v.findViewById(R.id.recylce_view_list_clients);
            layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ClientAdapter(clients, mdb);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }
        else{
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            nilClient = new ArrayList<>();
            Client client = new Client();
            client.setName("No data");
            nilClient.add(client);
            recyclerView = v.findViewById(R.id.recylce_view_list_clients);
            layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new ClientAdapter(nilClient, mdb);

            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);

        }
        clientMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // linearLayoutHome.setVisibility(View.GONE);
                openAddClient(v);
            }
        });
        searchtextview.addTextChangedListener(new EditTextSearchField(clients, adapter));

        return v;
    }
    public void openAddClient(View v){
        Fragment fragments = null;
        fragments = new AddClientFragment();
        if (fragments != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragments);
            ft.addToBackStack(null);
            ft.commit();
        }
    }


    public static class LoadClientList extends AsyncTask<Void, Void, List<Client>> {
        List<Client> client = new ArrayList<>();
        AppDatabase mdb;

        public LoadClientList(AppDatabase mdb) {
            this.mdb = mdb;
        }

        @Override
        protected List<Client> doInBackground(Void... voids) {

            client = mdb.clientRepository().getAll();

            return client;
        }


    }

}
