package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;

public class GetClientId extends AsyncTask<String, Void, Integer > {
    AppDatabase mdb;

    public GetClientId(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int id = mdb.clientRepository().getId(strings[0]);
        return id;
    }
}
