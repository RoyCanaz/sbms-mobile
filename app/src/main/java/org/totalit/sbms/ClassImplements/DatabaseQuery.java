package org.totalit.sbms.ClassImplements;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Branch;

import java.util.ArrayList;
import java.util.List;

public class DatabaseQuery {



    public static class GetId extends AsyncTask<Void, Void, Integer > {
        String name;
        AppDatabase mdb;


        public GetId(AppDatabase mdb, String name) {
            this.mdb = mdb;
            this.name = name;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            int id = mdb.categoryDao().getId(name);
            return id;
        }


    }
}
