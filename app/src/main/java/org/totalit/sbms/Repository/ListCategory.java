package org.totalit.sbms.Repository;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Category;

import java.util.ArrayList;
import java.util.List;

public class ListCategory extends AsyncTask<Void, Void, List<Category> > {
    List<Category> catNames = new ArrayList<>();
    AppDatabase mdb;


    public ListCategory(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected List<Category> doInBackground(Void... voids) {
        catNames = mdb.categoryDao().getNames();
        return catNames;
    }

}
