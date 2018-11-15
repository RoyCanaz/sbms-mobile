package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.ClientInventory;

import java.util.ArrayList;
import java.util.List;

public class ClientInventoryData extends AsyncTask<Void, Void, List<ClientInventory> > {

    List<ClientInventory> clientInventory = new ArrayList<>();
    AppDatabase mdb;
    public ClientInventoryData(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected List<ClientInventory> doInBackground(Void... voids) {

       // clientInventory = mdb.clientInventoryDao().getAllNotSynced();

        return clientInventory;
    }

}
