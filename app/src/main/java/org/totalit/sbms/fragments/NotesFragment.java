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
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.R;
import org.totalit.sbms.adapters.ContactsAdapter;
import org.totalit.sbms.adapters.NotesAdapter;
import org.totalit.sbms.domain.Notes;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NotesFragment extends Fragment {

    TextView nameOfClientProfile;
    FloatingActionButton addNoteFab;
    int clientDbId;
    int uuid;
    String clientName;
    AppDatabase mdb;
    RecyclerView recyclerView;
    NotesAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    List<Notes> notes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notes_fragment, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientDbId = this.getArguments().getInt("clientId");
        uuid = this.getArguments().getInt("uuid");
        clientName = this.getArguments().getString("clientName");
        addNoteFab = view.findViewById(R.id.fab_new_note);
        nameOfClientProfile = view.findViewById(R.id.name_of_client_notes);
        nameOfClientProfile.setText(clientName);

        try {
            notes = new LoadNotes(mdb, clientDbId, uuid).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        recyclerView = view.findViewById(R.id.recycler_view_notes);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NotesAdapter(notes);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                Bundle bundle = new Bundle();
                fragment = new AddNoteFragment();
                bundle.putInt("clientId", clientDbId);
                bundle.putInt("uuid", uuid);
                bundle.putString("clientName", clientName);
                fragment.setArguments(bundle);
                openFragment(fragment);
            }
        });
        if(notes.size()==0){
            Toast.makeText(getActivity().getApplicationContext(), "No Notes Found", Toast.LENGTH_LONG).show();
        }

        return view;
    }
    private void openFragment(Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    public class LoadNotes extends AsyncTask<Void, Void, List<Notes>> {

        AppDatabase mdb;
        int id;
        int uuid;

        public LoadNotes(AppDatabase mdb, int id, int uuid) {
            this.id = id;
            this.mdb = mdb;
            this.uuid = uuid;
        }

        @Override
        protected List<Notes> doInBackground(Void... voids) {
            if(uuid==0) {
                List<Notes> notes = mdb.notesDao().findByClientId(id);
                return notes;
            }
            List<Notes> notes = mdb.notesDao().findByClientId(uuid);
            return notes;
        }


    }
}
