package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.ClientInventory;
import org.totalit.sbms.domain.ProcumentDocs;

import java.util.ArrayList;
import java.util.List;

public class ProcurementDocsData extends AsyncTask<Void, Void, List<ProcumentDocs>> {
    List<ProcumentDocs> procumentDocs = new ArrayList<>();
    AppDatabase mdb;
    public ProcurementDocsData(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected List<ProcumentDocs> doInBackground(Void... voids) {
       // procumentDocs = mdb.procumentDocsDao().getAllNotSynced();
        return procumentDocs;
    }
}
