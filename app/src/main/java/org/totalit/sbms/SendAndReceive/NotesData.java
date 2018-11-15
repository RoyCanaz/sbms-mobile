package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Notes;

import java.util.ArrayList;
import java.util.List;

public class NotesData extends AsyncTask<Void, Void, List<Notes>> {
    AppDatabase mdb;
    List<Notes> notesList = new ArrayList<>();

    public NotesData(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected List<Notes> doInBackground(Void... voids) {
      //  notesList = mdb.notesDao().getAllNotSynced();
        return notesList;
    }
}
