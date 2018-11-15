package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Client;

public class GetClientByRealId extends AsyncTask<Integer, Void, Client> {

    AppDatabase mdb;
    Client client;

    public GetClientByRealId(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected Client doInBackground(Integer... integers) {
        client = mdb.clientRepository().findOneByRealId(integers[0]);
        return client;
    }
}
