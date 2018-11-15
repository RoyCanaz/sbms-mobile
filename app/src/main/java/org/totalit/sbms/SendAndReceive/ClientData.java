package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientData extends AsyncTask<Void, Void, List<Client> > {
    List<Client> client = new ArrayList<>();
    AppDatabase mdb;

    public ClientData(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected List<Client> doInBackground(Void... voids) {

        client =  mdb.clientRepository().getAllNotSynced();

        return client;
    }

    @Override
    protected void onPostExecute(List<Client> clients) {
        super.onPostExecute(clients);
    }
}
