package org.totalit.sbms.Repository;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Supplier;

import java.util.ArrayList;
import java.util.List;

public class GetSupplierByName extends AsyncTask<Void, Void, Supplier > {
    Supplier supplier;
    AppDatabase mdb;
    String name;

    public GetSupplierByName(AppDatabase mdb, String name) {
        this.mdb = mdb;
        this.name = name;
    }

    public GetSupplierByName(AppDatabase mdb) {

        this.mdb = mdb;
    }

    @Override
    protected Supplier doInBackground(Void... voids) {
        supplier = mdb.supplierDao().getSupplierByName(name);
        return supplier;
    }
}
