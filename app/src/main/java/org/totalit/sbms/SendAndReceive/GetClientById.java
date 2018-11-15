package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Client;

import java.util.ArrayList;
import java.util.List;

public class GetClientById extends AsyncTask<Integer, Void, Client> {
    AppDatabase mdb;
    Client client;

    public GetClientById(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected Client doInBackground(Integer... integers) {
        client = mdb.clientRepository().findOneById(integers[0]);
        return client;
    }
}
