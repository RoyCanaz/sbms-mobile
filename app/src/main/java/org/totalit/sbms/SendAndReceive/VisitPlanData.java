package org.totalit.sbms.SendAndReceive;

import android.os.AsyncTask;

import org.totalit.sbms.Database.AppDatabase;
import org.totalit.sbms.domain.VisitPlan;

import java.util.ArrayList;
import java.util.List;

public class VisitPlanData extends AsyncTask<Void, Void, List<VisitPlan>> {
    AppDatabase mdb;
    List<VisitPlan> planList = new ArrayList<>();


    public VisitPlanData(AppDatabase mdb) {
        this.mdb = mdb;
    }
/*
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        planList = mdb.visitPlanDao().getAllNotSynced();
        for (VisitPlan plan ; planList){
            if(plan)
        }
    }
*/
    @Override
    protected List<VisitPlan> doInBackground(Void... voids) {

       // planList = mdb.visitPlanDao().getAllNotSynced();
        return planList;
    }
}
