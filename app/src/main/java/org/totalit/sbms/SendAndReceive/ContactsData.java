package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Contact;
import org.totalit.sbms.domain.ProcumentDocs;

import java.util.ArrayList;
import java.util.List;

public class ContactsData extends AsyncTask<Void, Void, List<Contact>> {
    List<Contact> con = new ArrayList<>();
    AppDatabase mdb;
    public ContactsData(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected List<Contact> doInBackground(Void... voids) {
      // con = mdb.contactDao().getAllNotSynced();
       return con;
    }
}
