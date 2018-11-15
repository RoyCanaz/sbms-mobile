package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.R;
import org.totalit.sbms.domain.Branch;
import org.totalit.sbms.domain.Client;

import java.util.ArrayList;
import java.util.List;

public class BranchData {
    public class BranchById extends AsyncTask<Void, Void, List<Branch> > {
        List<Branch> branches = new ArrayList<>();
        AppDatabase mdb;
        int clientId;

        public BranchById(AppDatabase mdb, int clientId) {
            this.mdb = mdb;
            this.clientId = clientId;
        }

        @Override
        protected List<Branch> doInBackground(Void... voids) {

          branches = mdb.branchDao().getAllByClientId(clientId);
          return branches;
        }

    }
    public static class BranchByNotSynced extends AsyncTask<Void, Void, List<Branch> > {
        List<Branch> branches = new ArrayList<>();
        AppDatabase mdb;
        int clientId;

        public BranchByNotSynced(AppDatabase mdb) {
            this.mdb = mdb;
            this.clientId = clientId;
        }

        @Override
        protected List<Branch> doInBackground(Void... voids) {

          //  branches = mdb.branchDao().getAllNotSynced();
            return branches;
        }

    }

}
