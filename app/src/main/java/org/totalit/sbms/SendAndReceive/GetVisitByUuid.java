package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.VisitPlan;

import java.util.List;

public class GetVisitByUuid extends AsyncTask<Integer, Void, VisitPlan> {
    AppDatabase mdb;
    VisitPlan visitPlan;

    public GetVisitByUuid(AppDatabase mdb) {
        this.mdb = mdb;
    }

    @Override
    protected VisitPlan doInBackground(Integer... integers) {
        visitPlan = mdb.visitPlanDao().getByRealId(integers[0]);
        return visitPlan;
    }
}
