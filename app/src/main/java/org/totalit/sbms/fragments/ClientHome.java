package org.totalit.sbms.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.R;
import org.totalit.sbms.domain.ClientInventory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientHome extends Fragment {
    String clientName;
    int clientDbId;
    int uuid;
    TextView clientFullname;
    Button btnProfile, btnContacts, btnInventory, btnNotes;
    AppDatabase mdb;
    LinearLayout root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_options, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientFullname = view.findViewById(R.id.name_of_client);
        btnProfile = view.findViewById(R.id.btn_profile);
        btnContacts = view.findViewById(R.id.btn_contacts);
        btnInventory = view.findViewById(R.id.btn_inventory);
        btnNotes = view.findViewById(R.id.btn_notes);
        root = view.findViewById(R.id.linear_root_client_home);
        clientName = this.getArguments().getString("clientFullName");
        clientDbId = this.getArguments().getInt("clientDbId");
        uuid = this.getArguments().getInt("uuid");
        clientFullname.setText(clientName);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Fragment fragment = new ClientProfileFragment();
                bundle.putInt("clientId", clientDbId);
                bundle.putInt("uuid", uuid);
                bundle.putString("clientName", clientName);
                fragment.setArguments(bundle);
                navigateTo(fragment);
            }
        });
        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Fragment fragment = new ClientContactsFragment();
                bundle.putInt("clientId", clientDbId);
                bundle.putInt("uuid", uuid);
                bundle.putString("clientName", clientName);
                fragment.setArguments(bundle);
                navigateTo(fragment);
            }
        });
        btnInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    Fragment fragment = new ClientInventoriesFragment();
                    bundle.putInt("clientId", clientDbId);
                    bundle.putInt("uuid", uuid);
                    bundle.putString("clientName", clientName);
                    fragment.setArguments(bundle);
                    navigateTo(fragment);

            }
        });
        btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                Fragment fragment = new NotesFragment();
                bundle.putInt("clientId", clientDbId);
                bundle.putInt("uuid", uuid);
                bundle.putString("clientName", clientName);
                fragment.setArguments(bundle);
                navigateTo(fragment);

            }
        });


        return view;
    }




    private void navigateTo(Fragment fragment){


        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
