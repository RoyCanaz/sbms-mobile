package org.totalit.sbms.Repository;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.Category;
import org.totalit.sbms.domain.Supplier;

import java.util.ArrayList;
import java.util.List;

public class SupplierList extends AsyncTask<Void, Void, List<Supplier> > {
    List<Supplier> suppliers = new ArrayList<>();
    AppDatabase mdb;

    public SupplierList(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected List<Supplier> doInBackground(Void... voids) {
        suppliers = mdb.supplierDao().getSuppliers();
        return suppliers;
    }
}
