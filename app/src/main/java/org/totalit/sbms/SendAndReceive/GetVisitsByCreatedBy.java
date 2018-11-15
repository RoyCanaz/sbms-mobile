package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.VisitPlan;

import java.util.ArrayList;
import java.util.List;

public class GetVisitsByCreatedBy extends AsyncTask<Integer, Void, List<VisitPlan>> {
    AppDatabase mdb;
    List<VisitPlan> planList = new ArrayList<>();

    public GetVisitsByCreatedBy(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected List<VisitPlan> doInBackground(Integer... integers) {
        planList = mdb.visitPlanDao().findByCreatedBy(integers[0]);

        return planList;
    }
}
