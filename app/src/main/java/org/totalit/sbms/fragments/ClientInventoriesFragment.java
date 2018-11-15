package org.totalit.sbms.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.R;
import org.totalit.sbms.adapters.InventoryAdapter;
import org.totalit.sbms.domain.ClientInventory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientInventoriesFragment extends Fragment {

    TextView nameOfClientInvent;
    int clientDbId;
    int uuid;
    String clientName;
    AppDatabase mdb;
    RecyclerView recyclerView;
    InventoryAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton addInventoryFab;
    List<ClientInventory> inventories;
    LinearLayout root;
    Snackbar snackbar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_inventory, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientDbId = this.getArguments().getInt("clientId");
        uuid = this.getArguments().getInt("uuid");
        clientName = this.getArguments().getString("clientName");
        nameOfClientInvent = view.findViewById(R.id.name_of_client_inventory);
        addInventoryFab = view.findViewById(R.id.fab_new_inventory);
        //root = view.findViewById(R.id.linear_root_inventories);
        nameOfClientInvent.setText(clientName);

        try {
            inventories = new LoadClientInventory(mdb, clientDbId, uuid).execute().get();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        recyclerView = view.findViewById(R.id.recycler_view_inventory);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new InventoryAdapter(inventories);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        addInventoryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                fragment = new NewInventoryFragment();
                bundle.putInt("clientId", clientDbId);
                bundle.putInt("uuid", uuid);
                bundle.putString("clientName", clientName);
                fragment.setArguments(bundle);
                openFragment(fragment);
            }
        });
        if(inventories.size()==0){
            Toast.makeText(getActivity().getApplicationContext(), "No Inventory Found", Toast.LENGTH_LONG).show();
            //snackbar = Snackbar.make(root.getRootView(), "No Inventory Found", Snackbar.LENGTH_LONG);
         //   Snackbar.make(view.findViewById(R.id.linear_root_inventories), "No Inventory Found", Snackbar.LENGTH_LONG);
            //  snackbar.show();
        }
        return  view;
    }

    private void openFragment(Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public class LoadClientInventory extends AsyncTask<Void, Void, List<ClientInventory>> {

        AppDatabase mdb;
        int id;
        int uuid;

        public LoadClientInventory(AppDatabase mdb, int id, int uuid) {
            this.id = id;
            this.mdb = mdb;
            this.uuid = uuid;
        }

        @Override
        protected List<ClientInventory> doInBackground(Void... voids) {
            if(uuid==0) {
                List<ClientInventory> clientInventories = mdb.clientInventoryDao().findByClientId(id);
                return clientInventories;
            }
            List<ClientInventory> clientInventories = mdb.clientInventoryDao().findByClientId(uuid);
            return clientInventories;
        }


    }

}
