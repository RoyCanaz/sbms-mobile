package org.totalit.sbms.Repository;

import android.os.AsyncTask;
import android.util.Log;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Quote;

import java.util.ArrayList;
import java.util.List;

public  class QuoteList extends AsyncTask<Void, Void, List<Quote> > {

    AppDatabase mdb;

    public QuoteList(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected List<Quote> doInBackground(Void... voids) {
        Log.e("Data", "Retrivieng Quoute List.........");
        List<Quote> quoteList = new ArrayList<>();
        quoteList = mdb.quoteDao().getQuotes();
        return quoteList;
    }
}
