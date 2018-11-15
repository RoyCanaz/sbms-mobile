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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.LoginActivity;
import org.totalit.sbms.R;
import org.totalit.sbms.SendAndReceive.GetClientById;
import org.totalit.sbms.Utils.SyncStatus;
import org.totalit.sbms.domain.Client;
import org.totalit.sbms.domain.Notes;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class AddNoteFragment extends Fragment {
    private EditText note;
    private TextView cName;
    private Button saveNote;
    private int clientId;
    private int uuid;
    String clientName;
    SharedPreferences sharedPreferences;
    AppDatabase mdb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_notes_fragment, container, false);
        mdb = AppDatabase.getFileDatabase(getActivity().getApplicationContext());
        clientId = this.getArguments().getInt("clientId");
        uuid = this.getArguments().getInt("uuid");
        clientName = this.getArguments().getString("clientName");
        sharedPreferences = getActivity().getSharedPreferences(LoginActivity.loginPref, Context.MODE_PRIVATE);
        note = view.findViewById(R.id.editText);
        saveNote = view.findViewById(R.id.save_note_btn);
        cName = view.findViewById(R.id.title_add_note_client_name);
        cName.setText(clientName);
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addNote(v);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    public void addNote(View view) throws ExecutionException, InterruptedException {
        final Notes notes = new Notes();
        Date date = new Date();

        notes.setRealId(0);
        notes.setDateCreated(date.toString());
        notes.setSyncStatus(SyncStatus.NOT_SEND);
        notes.setNote(note.getText().toString());
        if(uuid==0) {
            notes.setClientId(clientId);
        }
        else{
            notes.setClientId(uuid);
        }
        notes.setCreatedBy(sharedPreferences.getInt(LoginActivity.idPref, 0));

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mdb.notesDao().insertNote(notes);
            }
        });
        Toast.makeText(getActivity().getApplicationContext(), "Note Saved successfully", Toast.LENGTH_SHORT).show();
        note.setText("");
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putInt("clientId", clientId);
        bundle.putInt("uuid", uuid);
        bundle.putString("clientName", clientName);

        fragment = new NotesFragment();
        fragment.setArguments(bundle);



        if (fragment != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
