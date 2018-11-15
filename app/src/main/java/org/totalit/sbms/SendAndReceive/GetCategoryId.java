package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;

public class GetCategoryId extends AsyncTask<String, Void, Integer > {
    AppDatabase mdb;

    public GetCategoryId(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        int id = mdb.categoryDao().getId(strings[0]);
        return id;
    }
}
